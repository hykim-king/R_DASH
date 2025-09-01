package com.pcwk.ehr.service;

public interface MailService {

	//인증 코드 전송
	public void sendcode(String email);
	//코드 확인
	public int verify(String email, String codeInput);
	//비밀번호 초기화 후 메일 전송
	public void resetPwToMail(String email);
}
