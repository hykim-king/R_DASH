package com.pcwk.ehr.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

	// 모델/샘플링
	private static final String MODEL = "gpt-5-nano";
	private static final double TEMP = 0.7;
	private static final int MAX_TOKENS = 800;

	// 컨텍스트/안전장치
	private static final int HISTORY_LIMIT = 10; // 최근 N개 Q/A 페어
	private static final int SNIPPET_LIMIT = 2000; // 이력/질문 프롬프트 안전 자르기
	private static final int DB_TEXT_LIMIT = 2000; // NVARCHAR2(2000) 저장 한도

	private final OkHttpClient http;
	private final ObjectMapper om = new ObjectMapper();
	private final String apiKey;

	private final ChatMapper chatMapper; // 이력 조회용
	private final ChatService chatService; // 저장용

	public GptBotService(ChatMapper chatMapper, ChatService chatService) {
		this.chatMapper = chatMapper;
		this.chatService = chatService;

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

		// Java 8: isBlank() 대신 trim().isEmpty()
		if (sessionId == null || sessionId.trim().isEmpty()) {
			sessionId = UUID.randomUUID().toString();
		}

		try {
			// 1) 세션 이력 로드 (오래된 → 최신)
			List<ChatDTO> ctx = fetchHistory(sessionId, userNo, HISTORY_LIMIT);

			// 2) OpenAI 호출
			String answer = callOpenAI(ctx, question);

			// 3) DB 저장(질문/답변 안전컷)
			String qSave = safeDb(question);
			String aSave = safeDb(answer);
			try {
				ChatDTO row = new ChatDTO(null, userNo, sessionId, qSave, aSave, null);
				chatService.insertChat(row);
				log.debug("대화 저장 완료: logNo={}", row.getLogNo());
			} catch (Exception e) {
				log.error("대화 저장 실패", e); // 저장 실패해도 사용자 응답은 반환
			}

			return answer;

		} catch (IOException e) {
			log.error("OpenAI 호출 실패", e);
			return "일시적인 문제로 답변 생성에 실패했습니다. 잠시 후 다시 시도해 주세요.";
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("OpenAI 호출 중단", e);
			return "요청이 중단되었습니다. 다시 시도해 주세요.";
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
		Collections.reverse(rows); // 최신→오래된 으로 온 것을 뒤집어 오래된→최신으로
		return rows;
	}

	/** OpenAI 호출 (429/5xx 재시도 포함) */
	private String callOpenAI(List<ChatDTO> history, String question) throws IOException, InterruptedException {
		String body = buildRequestBody(history, question);

		Request req = new Request.Builder().url(API_URL).addHeader("Authorization", "Bearer " + apiKey)
				.addHeader("Content-Type", "application/json").post(RequestBody.create(body, JSON)).build();

		// 지수 백오프(0ms, 600ms, 1500ms)
		int[] waits = { 0, 600, 1500 };
		IOException last = null;

		for (int i = 0; i < waits.length; i++) {
			if (waits[i] > 0)
				Thread.sleep(waits[i]);

			Response resp = null;
			try {
				resp = http.newCall(req).execute();
				if (resp.isSuccessful()) {
					String json = resp.body() != null ? resp.body().string() : "";
					return extractAnswer(json);
				}
				int code = resp.code();
				String err = resp.body() != null ? resp.body().string() : "";
				log.warn("OpenAI HTTP {}: {}", code, err);

				// 429/5xx만 재시도
				if (!(code == 429 || code >= 500)) {
					return mapHttpError(code);
				}
			} catch (IOException e) {
				last = e; // 네트워크 예외 → 재시도
			} finally {
				if (resp != null)
					resp.close();
			}
		}
		if (last != null)
			throw last;
		return "유효한 AI 응답을 받지 못했습니다.";
	}

	/** 요청 바디 JSON 문자열 구성(ObjectMapper 사용) */
	private String buildRequestBody(List<ChatDTO> history, String currentQuestion) {
		ObjectNode root = om.createObjectNode();
		root.put("model", MODEL);
		root.put("temperature", TEMP);
		root.put("max_tokens", MAX_TOKENS);

		ArrayNode messages = om.createArrayNode();

		// system 프롬프트
		messages.add(msg("system",
				"You are a helpful assistant for a Korean disaster information site '재민이'. "
						+ "Answer in Korean by default, concise but accurate. "
						+ "If unsure, say you are unsure. Be safe and non-alarming."));

		// 과거 이력 user → assistant 페어
		for (ChatDTO row : history) {
			if (row.getQuestion() != null && !row.getQuestion().trim().isEmpty()) {
				messages.add(msg("user", safeSnippet(row.getQuestion())));
			}
			if (row.getAnswer() != null && !row.getAnswer().trim().isEmpty()) {
				messages.add(msg("assistant", safeSnippet(row.getAnswer())));
			}
		}

		// 현재 질문
		messages.add(msg("user", safeSnippet(currentQuestion)));

		root.set("messages", messages);
		return root.toString();
	}

	private ObjectNode msg(String role, String content) {
		ObjectNode n = om.createObjectNode();
		n.put("role", role);
		n.put("content", content);
		return n;
	}

	/** OpenAI 응답에서 첫 메시지 content 추출 */
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

	/** 이력/프롬프트용 안전 스니펫 */
	private String safeSnippet(String s) {
		if (s == null)
			return null;
		String t = s.trim();
		return (t.length() > SNIPPET_LIMIT) ? (t.substring(0, SNIPPET_LIMIT) + "...") : t;
	}

	/** DB 저장용 안전 컷(NVARCHAR2 2000) */
	private String safeDb(String s) {
		if (s == null)
			return null;
		return (s.length() > DB_TEXT_LIMIT) ? s.substring(0, DB_TEXT_LIMIT) : s;
	}
}