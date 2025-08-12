package com.pcwk.ehr.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.mapper.ChatMapper;
import com.pcwk.ehr.service.BotService;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;

@Service("gptBotService") // 컨트롤러의 @Qualifier("gptBotService")와 일치
@Primary // 우선 주입
public class LcBotService implements BotService {

	private static final Logger log = LogManager.getLogger(LcBotService.class);

	private static final int HISTORY_LIMIT = 10; // 최근 Q/A 페어 수
	private static final int SNIPPET_LIMIT = 2000; // NVARCHAR2(2000) 가드

	private final ChatLanguageModel model;
	private final ChatMapper chatMapper;

	public LcBotService(ChatLanguageModel model, ChatMapper chatMapper) {
		this.model = model;
		this.chatMapper = chatMapper;
	}

	@Override
	public String reply(String question, String sessionId, Integer userNo) {
		if (question == null || question.trim().isEmpty()) {
			return "질문이 비어 있습니다. 다시 입력해 주세요.";
		}
		if (model == null) {
			return "AI 모델이 초기화되지 않았습니다.";
		}

		String q = question.trim();

		// 1) 대화 이력 로드 (오래된 → 최신)
		List<ChatDTO> history = fetchHistory(sessionId, userNo, HISTORY_LIMIT);

		// 2) LangChain 메시지 빌드 (system → 과거 user/ai → 현재 user)
		List<ChatMessage> msgs = new ArrayList<ChatMessage>();
		msgs.add(SystemMessage.from("You are a helpful assistant that answers in Korean. "
				+ "Keep answers concise but include key details. If unsure, say so."));

		for (ChatDTO row : history) {
			String hq = safe(row.getQuestion());
			String ha = safe(row.getAnswer());
			if (hq != null && !hq.isEmpty())
				msgs.add(UserMessage.from(hq));
			if (ha != null && !ha.isEmpty())
				msgs.add(AiMessage.from(ha));
		}
		msgs.add(UserMessage.from(safe(q)));

		// 3) 호출
		String answer;
		try {
			Response<AiMessage> resp = model.generate(msgs);
			answer = (resp != null && resp.content() != null) ? resp.content().text() : "";
			if (answer == null || answer.trim().isEmpty()) {
				answer = "유효한 AI 응답을 받지 못했습니다.";
			}
		} catch (Exception e) {
			log.error("LangChain generate 실패", e);
			return "일시적인 문제로 답변 생성에 실패했습니다. 잠시 후 다시 시도해 주세요.";
		}

		// 4) DB 저장 (세션/유저 null 허용)
		ChatDTO row = new ChatDTO(null, userNo, sessionId, safe(q), safe(answer), null);
		try {
			// ChatService 대신 매퍼 직접 사용해도 되고, 기존처럼 ChatService 써도 됩니다.
			chatMapper.insertChat(row);
		} catch (Exception e) {
			log.warn("대화 저장 실패(무시): {}", e.getMessage());
		}

		return answer;
	}

	private List<ChatDTO> fetchHistory(String sessionId, Integer userNo, int limit) {
		if (sessionId == null || sessionId.trim().isEmpty())
			return Collections.emptyList();
		List<ChatDTO> rows = chatMapper.findRecentBySession(sessionId, userNo, limit);
		if (rows == null || rows.isEmpty())
			return Collections.emptyList();
		Collections.reverse(rows); // 오래된 → 최신
		return rows;
	}

	private static String safe(String s) {
		if (s == null)
			return null;
		String t = s.trim();
		if (t.length() > SNIPPET_LIMIT)
			t = t.substring(0, SNIPPET_LIMIT) + "...";
		return t;
	}
}