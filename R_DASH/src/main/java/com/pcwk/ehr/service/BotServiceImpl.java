package com.pcwk.ehr.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BotServiceImpl implements BotService {
	
	private static final Logger log = LogManager.getLogger(BotServiceImpl.class);

	private static final Pattern GREET = Pattern.compile(".*(안녕|hello|hi).*", Pattern.CASE_INSENSITIVE);
	private static final int MAX_Q_LEN = 300;
	private static final int ECHO_SNIPPET = 200;
	private static final ZoneId KST = ZoneId.of("Asia/Seoul");
	private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Override
	public String reply(String question, String sessionId, Integer userNo) {
		log.debug("질문: {}", question);
		log.debug("세션 ID: {}, 사용자 번호: {}", sessionId, userNo);

		if (question == null || question.trim().isEmpty()) {
			log.warn("빈 질문 수신");
			return "질문이 비어 있습니다. 다시 입력해 주세요.";
		}

		String q = question.trim();
		String lower = q.toLowerCase(Locale.ROOT);

		String rule = getRuleBasedAnswer(q, lower);
		if (rule != null)
			return rule;

		String safe = q.length() > ECHO_SNIPPET ? q.substring(0, ECHO_SNIPPET) + "..." : q;
		return "질문을 잘 받았어요: \"" + safe + "\"";
	}

	private String getRuleBasedAnswer(String original, String lower) {
		if (GREET.matcher(lower).matches())
			return "안녕하세요! 무엇을 도와드릴까요?";
		if (lower.contains("시간") || lower.contains("지금 몇시")) {
			return "현재 시각은 " + LocalDateTime.now(KST).format(TS) + " 입니다.";
		}
		if (original.length() > MAX_Q_LEN)
			return "질문이 길어요. 핵심만 요약해서 다시 적어주실래요?";
		return null;
	}
}