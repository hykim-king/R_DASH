package com.pcwk.ehr.service;

public interface BotService {

	String reply(String question, String sessionId, Integer userNo);

	// ▼ 저장 여부 선택(게스트= false)
	default String reply(String question, String sessionId, Integer userNo, boolean persist) {
		// 구현체가 없으면 기본은 기존 동작 유지
		return reply(question, sessionId, userNo);
	}

	// (선택) 향후 비동기/스트리밍 대비
	// CompletableFuture<String> replyAsync(String question, String sessionId,
	// Integer userNo);
}