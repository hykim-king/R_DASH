package com.pcwk.ehr.service;

public interface BotService {
	/**
	 * 사용자의 질문에 대한 챗봇 답변을 생성한다.
	 * 
	 * @param question  사용자 질문(필수, 공백 불가)
	 * @param sessionId 세션 식별자(로그 컨텍스트 용도)
	 * @param userNo    회원 번호(비회원이면 null)
	 * @return 생성된 답변(절대 null 아님)
	 * @throws BotServiceException 예외 상황(입력 오류/내부 오류 등)
	 */
	String reply(String question, String sessionId, Integer userNo);
}