package com.pcwk.ehr.service;

public interface BotService {
	
	String reply(String question, String sessionId, Integer userNo);

	// 편의: 세션/유저 없이도 호출
	default String reply(String question) {
		return reply(question, null, null);
	}

	// (선택) 향후 비동기/스트리밍 대비
	// CompletableFuture<String> replyAsync(String question, String sessionId,
	// Integer userNo);
}