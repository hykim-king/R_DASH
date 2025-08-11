package com.pcwk.ehr.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.ChatDTO;

@Service
public class BotServiceImpl implements BotService {

	private static final Logger log = LogManager.getLogger(BotServiceImpl.class);

	private static final Pattern GREET = Pattern.compile(".*(안녕|hello|hi).*", Pattern.CASE_INSENSITIVE);//인사말 패턴(대소문자 무시). 문자열 어디에 들어가도 매칭되게 .*….* 형태.
	private static final int MAX_Q_LEN = 300; // 너무 긴 질문 차단(논리적 제한)
	private static final int DB_MAX_TEXT = 2000; // NVARCHAR2(2000) 최대 길이
	private static final int ECHO_SNIPPET = 200; // 에코 응답에 포함할 질문 프리뷰 길이
	private static final ZoneId KST = ZoneId.of("Asia/Seoul"); //한국 시간과 표시 포맷 고정(서버 타임존과 무관하게 일관된 결과 보장).
	private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private final ChatService chatService;

	public BotServiceImpl(ChatService chatService) {
		this.chatService = chatService;
	}

	@Override
	public String reply(String question, String sessionId, Integer userNo) {
		log.debug("질문: {}", question);
		log.debug("세션 ID: {}, 사용자 번호: {}", sessionId, userNo);//들어온 파라미터를 로깅(운영에선 개인정보나 과도한 길이의 로깅은 주의!).

		if (question == null || question.trim().isEmpty()) {
			log.warn("빈 질문 수신");
			return "질문이 비어 있습니다. 다시 입력해 주세요.";//필수 검증: 비어 있는 질문은 즉시 사용자 안내로 반환.
		}

		String q = question.trim();//앞뒤 공백 제거.
		String lower = q.toLowerCase(Locale.ROOT);//룰 체크용으로 소문자 변환(영문 키워드 매칭 안정화; 한국어엔 영향 없음).

		// 1) 규칙 기반 빠른 응답
		String answer = getRuleBasedAnswer(q, lower);//1차: 규칙 기반 응답(인사/시간/길이 초과)을 시도.
		if (answer == null) {
			// 2) 기본 에코(또는 여기서 LLM 호출 자리)
			String safe = q.length() > ECHO_SNIPPET ? q.substring(0, ECHO_SNIPPET) + "..." : q;
			answer = "질문을 잘 받았어요: \"" + safe + "\"";
		}

		// 3) DB 저장 (세션/유저는 null 허용)
		ChatDTO logRow = new ChatDTO(null, userNo, sessionId, cut(q, DB_MAX_TEXT), cut(answer, DB_MAX_TEXT), null);
		try {
			chatService.insertChat(logRow);
			log.debug("대화 저장 완료: logNo={}", logRow.getLogNo());
		} catch (Exception e) {
			log.error("대화 저장 실패", e);
			// 저장 실패해도 사용자 응답은 반환
		}

		return answer;
	}

	private String getRuleBasedAnswer(String original, String lower) {
		if (GREET.matcher(lower).matches())
			return "안녕하세요! 무엇을 도와드릴까요?";
		if (lower.contains("시간") || lower.contains("지금 몇시"))
			return "현재 시각은 " + LocalDateTime.now(KST).format(TS) + " 입니다.";
		if (original.length() > MAX_Q_LEN)
			return "질문이 길어요. 핵심만 요약해서 다시 적어주실래요?";
		return null;
	}

	private static String cut(String s, int max) {
		if (s == null)
			return null;
		return s.length() > max ? s.substring(0, max) : s;
	}
}