package com.pcwk.ehr.domain;

public class ChatSessionSummary {

	private String sessionId;
	private String title; // 첫 질문 30자
	private String lastMsg; // 마지막 답변/질문 30자
	private String updatedAt; // 최근 대화 시각 (yyyy-MM-dd HH:mm:ss)
	private Long lastLogNo; // 무한스크롤 경계값

	/**
	 * 
	 */
	public ChatSessionSummary() {
	}

	/**
	 * @param sessionId
	 * @param title
	 * @param lastMsg
	 * @param updatedAt
	 * @param lastLogNo
	 */
	public ChatSessionSummary(String sessionId, String title, String lastMsg, String updatedAt, Long lastLogNo) {
		super();
		this.sessionId = sessionId;
		this.title = title;
		this.lastMsg = lastMsg;
		this.updatedAt = updatedAt;
		this.lastLogNo = lastLogNo;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the lastMsg
	 */
	public String getLastMsg() {
		return lastMsg;
	}

	/**
	 * @param lastMsg the lastMsg to set
	 */
	public void setLastMsg(String lastMsg) {
		this.lastMsg = lastMsg;
	}

	/**
	 * @return the updatedAt
	 */
	public String getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the lastLogNo
	 */
	public Long getLastLogNo() {
		return lastLogNo;
	}

	/**
	 * @param lastLogNo the lastLogNo to set
	 */
	public void setLastLogNo(Long lastLogNo) {
		this.lastLogNo = lastLogNo;
	}

	@Override
	public String toString() {
		return "ChatSessionSummary [sessionId=" + sessionId + ", title=" + title + ", lastMsg=" + lastMsg
				+ ", updatedAt=" + updatedAt + ", lastLogNo=" + lastLogNo + "]";
	}

}
