package com.pcwk.ehr.service;

public class FakeBotService implements BotService {

	@Override
	public String reply(String question, String sessionId, Integer userNo) {
		// 외부 호출 없이 고정 응답
		return "테스트 응답";
	}

}
