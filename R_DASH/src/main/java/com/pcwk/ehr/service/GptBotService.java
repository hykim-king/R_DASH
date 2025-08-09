package com.pcwk.ehr.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.mapper.ChatMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service("gptBotService")
@Primary
public class GptBotService implements BotService {

	private static final Logger log = LogManager.getLogger(GptBotService.class);

	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private static final String API_URL = "https://api.openai.com/v1/chat/completions";

	// ✅ GPT-5 nano
	private static final String MODEL = "gpt-5-nano";
	private static final double TEMP = 0.7;
	private static final int MAX_TOKENS = 800;

	// 대화 이력 관련
	private static final int HISTORY_LIMIT = 10; // 최근 N개 Q/A 페어
	private static final int SNIPPET_LIMIT = 2000; // 개별 문장 길이 안전장치

	private final OkHttpClient http;
	private final ObjectMapper om = new ObjectMapper();
	private final String apiKey;
	private final ChatMapper chatMapper;

	public GptBotService(ChatMapper chatMapper) {
		this.chatMapper = chatMapper;

		this.http = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS).build();

		String k = System.getenv("OPENAI_API_KEY");
		if (k == null || k.trim().isEmpty())
			k = System.getProperty("OPENAI_API_KEY");
		this.apiKey = k;
		if (this.apiKey == null || this.apiKey.trim().isEmpty()) {
			log.warn("OPENAI_API_KEY 미설정. GptBotService는 동작하지 않습니다.");
		}
	}

	@Override
	public String reply(String question, String sessionId, Integer userNo) {
		if (question == null || question.trim().isEmpty()) {
			return "질문이 비어 있습니다. 다시 입력해 주세요.";
		}
		if (apiKey == null || apiKey.trim().isEmpty()) {
			return "AI 키가 설정되지 않아 답변을 생성할 수 없습니다.";
		}

		try {
			// 1) 세션 이력 불러오기 (없으면 빈 리스트)
			List<ChatDTO> recent = fetchHistory(sessionId, userNo, HISTORY_LIMIT);

			// 2) messages 배열 구성 (system → 과거 user/assistant 페어 → 이번 user)
			String messagesJson = buildMessagesJson(recent, question);

			// 3) 최종 요청 바디
			String body = "{" + "\"model\":\"" + MODEL + "\"," + "\"temperature\":" + TEMP + "," + "\"max_tokens\":"
					+ MAX_TOKENS + "," + "\"messages\":" + messagesJson + "}";

			Request req = new Request.Builder().url(API_URL).addHeader("Authorization", "Bearer " + apiKey)
					.post(RequestBody.create(body, JSON)).build();

			try (Response resp = http.newCall(req).execute()) {
				if (!resp.isSuccessful()) {
					String err = resp.body() != null ? resp.body().string() : "";
					log.error("OpenAI HTTP {}: {}", resp.code(), err);
					return mapHttpError(resp.code());
				}
				String json = resp.body() != null ? resp.body().string() : "";
				return extractAnswer(json);
			}
		} catch (IOException e) {
			log.error("OpenAI 호출 실패", e);
			return "일시적인 문제로 답변 생성에 실패했습니다. 잠시 후 다시 시도해 주세요.";
		} catch (Exception e) {
			log.error("GPT 처리 오류", e);
			return "답변 처리 중 오류가 발생했습니다.";
		}
	}

	/** 세션/사용자 기준 최근 N건을 오래된→최신 순으로 반환 */
	private List<ChatDTO> fetchHistory(String sessionId, Integer userNo, int limit) {
		if (sessionId == null || sessionId.trim().isEmpty())
			return Collections.emptyList();
		List<ChatDTO> rows = chatMapper.findRecentBySession(sessionId, userNo, limit);
		if (rows == null || rows.isEmpty())
			return Collections.emptyList();
		// 쿼리는 최신순으로 가져오므로 역순 정렬해 "오래된→최신"으로 변환
		Collections.reverse(rows);
		return rows;
	}

	/** system + 과거 Q/A + 현재 질문 → messages JSON 문자열 생성 */
	private String buildMessagesJson(List<ChatDTO> history, String currentQuestion) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		// system 프롬프트
		sb.append("{\"role\":\"system\",\"content\":\"You are a helpful assistant that answers in Korean. "
				+ "Keep answers concise, but include key details. If unsure, say so.\"}");

		// 과거 이력(user → assistant 페어)
		for (ChatDTO row : history) {
			String q = safeSnippet(row.getQuestion());
			String a = safeSnippet(row.getAnswer());
			if (q != null && !q.isEmpty()) {
				sb.append(",{\"role\":\"user\",\"content\":").append(toJson(q)).append("}");
			}
			if (a != null && !a.isEmpty()) {
				sb.append(",{\"role\":\"assistant\",\"content\":").append(toJson(a)).append("}");
			}
		}

		// 현재 질문
		sb.append(",{\"role\":\"user\",\"content\":").append(toJson(safeSnippet(currentQuestion))).append("}");
		sb.append("]");
		return sb.toString();
	}

	private String extractAnswer(String json) throws IOException {
		JsonNode root = om.readTree(json);
		JsonNode choices = root.path("choices");
		if (choices.isArray() && choices.size() > 0) {
			String content = choices.get(0).path("message").path("content").asText("");
			if (!content.trim().isEmpty())
				return content;
		}
		return "유효한 AI 응답을 받지 못했습니다.";
	}

	private String mapHttpError(int code) {
		if (code == 401)
			return "AI 인증에 실패했습니다. API 키를 확인해 주세요.";
		if (code == 429)
			return "요청이 많습니다. 잠시 후 다시 시도해 주세요.";
		if (code >= 500)
			return "AI 서버 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.";
		return "AI 호출 실패(" + code + ")";
	}

	/** 문자열을 안전한 JSON string으로 인코딩 */
	private String toJson(String s) {
		try {
			return om.writeValueAsString(s);
		} catch (Exception e) {
			return "\"" + s.replace("\"", "\\\"") + "\"";
		}
	}

	/** 너무 긴 문장을 잘라 안전하게 사용 */
	private String safeSnippet(String s) {
		if (s == null)
			return null;
		String t = s.trim();
		if (t.length() > SNIPPET_LIMIT)
			t = t.substring(0, SNIPPET_LIMIT) + "...";
		return t;
	}
}