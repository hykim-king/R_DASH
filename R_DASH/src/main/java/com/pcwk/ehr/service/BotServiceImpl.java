package com.pcwk.ehr.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

@Service // 기본 구현체(교체 전까지 @Primary 없어도 단일 빈이면 주입됨)
public class BotServiceImpl implements BotService {

	@Override
	public String reply(String question, String sessionId, Integer userNo) {
		if (question == null || question.isEmpty()) {
			throw new BotServiceException("질문이 비어 있습니다.");
		}

		// 간단 룰 몇 가지
		String q = question.trim();
		if (q.matches("(?i).*(안녕|hello|hi).*")) {
			return "안녕하세요! 무엇을 도와드릴까요?";
		}
		if (q.contains("시간") || q.contains("지금 몇시")) {
			String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			return "현재 시각은 " + now + " 입니다.";
		}
		if (q.length() > 300) {
			return "질문이 길어요. 핵심만 요약해서 다시 적어주실래요?";
		}

		// 기본 에코 응답(운영 전/LLM 미연동 시 임시 동작)
		return "질문을 잘 받았어요: \"" + (q.length() > 200 ? q.substring(0, 200) + "..." : q) + "\"";
	}
}