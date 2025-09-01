package com.pcwk.ehr.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
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

	// ===== application.properties에서 주입 =====
	@Value("${openai.api-url:https://api.openai.com/v1/chat/completions}")
	private String apiUrl;

	@Value("${openai.model:gpt-4o}")
	private String model;

	@Value("${openai.temperature:0.7}")
	private double temperature;

	@Value("${openai.max-tokens:800}")
	private int maxTokens;

	// 조직 헤더가 필요한 경우만 사용
	@Value("${openai.org:}")
	private String organization;

	// 프로퍼티 → 환경변수 → 시스템 프로퍼티 순으로 키 해석
	@Value("${openai.api-key:}")
	private String propApiKey;

	// 컨텍스트/안전장치
	private static final int HISTORY_LIMIT = 10;
	private static final int SNIPPET_LIMIT = 2000;
	private static final int DB_TEXT_LIMIT = 2000;

	private final OkHttpClient http;
	private final ObjectMapper om = new ObjectMapper();

	private final ChatMapper chatMapper;
	private final ChatService chatService;

	public GptBotService(ChatMapper chatMapper, ChatService chatService) {
		this.chatMapper = chatMapper;
		this.chatService = chatService;

		this.http = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
				.writeTimeout(30, TimeUnit.SECONDS).build();
	}

	/** 키 우선순위: properties → env → system property */
	private String resolveApiKey() {
		String k = trimOrNull(propApiKey);
		if (k == null)
			k = trimOrNull(System.getenv("OPENAI_API_KEY"));
		if (k == null)
			k = trimOrNull(System.getProperty("OPENAI_API_KEY"));
		return k;
	}

	@Override
	public String reply(String question, String sessionId, Integer userNo) {
		if (question == null || question.trim().isEmpty()) {
			return "질문이 비어 있습니다. 다시 입력해 주세요.";
		}
		String apiKey = resolveApiKey();
		if (apiKey == null || apiKey.isEmpty()) {
			return "AI 키가 설정되지 않아 답변을 생성할 수 없습니다.";
		}

		if (sessionId == null || sessionId.trim().isEmpty()) {
			sessionId = UUID.randomUUID().toString();
		}

		try {
			List<ChatDTO> ctx = fetchHistory(sessionId, userNo, HISTORY_LIMIT);
			String answer = callOpenAI(ctx, question, apiKey);

			try {
				ChatDTO row = new ChatDTO(null, userNo, sessionId, safeDb(question), safeDb(answer), null);
				chatService.insertChat(row);
				log.debug("대화 저장 완료: logNo={}", row.getLogNo());
			} catch (Exception e) {
				log.error("대화 저장 실패", e);
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

	private List<ChatDTO> fetchHistory(String sessionId, Integer userNo, int limit) {
		if (sessionId == null || sessionId.trim().isEmpty())
			return Collections.emptyList();
		List<ChatDTO> rows = chatMapper.findRecentBySession(sessionId, userNo, limit);
		if (rows == null || rows.isEmpty())
			return Collections.emptyList();
		Collections.reverse(rows);
		return rows;
	}

	/** OpenAI 호출 (429/5xx 재시도 포함) */
	private String callOpenAI(List<ChatDTO> history, String question, String apiKey)
			throws IOException, InterruptedException {

		String body = buildRequestBody(history, question);

		Request.Builder rb = new Request.Builder().url(apiUrl).addHeader("Authorization", "Bearer " + apiKey)
				.addHeader("Content-Type", "application/json");
		if (organization != null && !organization.isEmpty()) {
			rb.addHeader("OpenAI-Organization", organization);
		}

		Request req = rb.post(RequestBody.create(body, JSON)).build();

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

				if (!(code == 429 || code >= 500)) {
					return mapHttpError(code);
				}
			} catch (IOException e) {
				last = e;
			} finally {
				if (resp != null)
					resp.close();
			}
		}
		if (last != null)
			throw last;
		return "유효한 AI 응답을 받지 못했습니다.";
	}

	/** 요청 바디 JSON */
	private String buildRequestBody(List<ChatDTO> history, String currentQuestion) {
		ObjectNode root = om.createObjectNode();
		root.put("model", model);
		root.put("temperature", temperature);
		root.put("max_tokens", maxTokens);

		ArrayNode messages = om.createArrayNode();

		messages.add(msg("system",
				"You are a helpful assistant for a Korean disaster information site '재민이'. "
						+ "Answer in Korean by default, concise but accurate. "
						+ "If unsure, say you are unsure. Be safe and non-alarming."));

		if (history != null) {
			for (ChatDTO row : history) {
				if (row == null)
					continue;
				if (row.getQuestion() != null && !row.getQuestion().trim().isEmpty()) {
					messages.add(msg("user", safeSnippet(row.getQuestion())));
				}
				if (row.getAnswer() != null && !row.getAnswer().trim().isEmpty()) {
					messages.add(msg("assistant", safeSnippet(row.getAnswer())));
				}
			}
		}

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

	private String safeSnippet(String s) {
		if (s == null)
			return null;
		String t = s.trim();
		return (t.length() > SNIPPET_LIMIT) ? (t.substring(0, SNIPPET_LIMIT) + "...") : t;
	}

	private String safeDb(String s) {
		if (s == null)
			return null;
		return (s.length() > DB_TEXT_LIMIT) ? s.substring(0, DB_TEXT_LIMIT) : s;
	}

	private static String trimOrNull(String s) {
		if (s == null)
			return null;
		String t = s.trim();
		return t.isEmpty() ? null : t;
	}
}