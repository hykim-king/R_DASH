package com.pcwk.ehr.domain;

public class ChatDTO {

	/** 기록 번호 (PK) */
	private Long logNo;

	/** 회원 번호 */
	private Integer userNo;

	/** 세션 아이디 */
	private String sessionId;

	/** 질문 */
	private String question;

	/** 답변 */
	private String answer;

	/** 등록일 */
	private String regDt;

	/**
	 * 
	 */
	public ChatDTO() {
	}

	/**
	 * @param logNo
	 * @param userNo
	 * @param sessionId
	 * @param question
	 * @param answer
	 * @param regDt
	 */
	public ChatDTO(Long logNo, Integer userNo, String sessionId, String question, String answer, String regDt) {
		super();
		this.logNo = logNo;
		this.userNo = userNo;
		this.sessionId = sessionId;
		this.question = question;
		this.answer = answer;
		this.regDt = regDt;
	}

	/**
	 * @return the logNo
	 */
	public Long getLogNo() {
		return logNo;
	}

	/**
	 * @param logNo the logNo to set
	 */
	public void setLogNo(Long logNo) {
		this.logNo = logNo;
	}

	/**
	 * @return the userNo
	 */
	public Integer getUserNo() {
		return userNo;
	}

	/**
	 * @param userNo the userNo to set
	 */
	public void setUserNo(Integer userNo) {
		this.userNo = userNo;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * @return the regDt
	 */
	public String getRegDt() {
		return regDt;
	}

	/**
	 * @param regDt the regDt to set
	 */
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	@Override
	public String toString() {
		return "ChatDTO [logNo=" + logNo + ", userNo=" + userNo + ", sessionId=" + sessionId + ", question=" + question
				+ ", answer=" + answer + ", regDt=" + regDt + "]";
	}

}