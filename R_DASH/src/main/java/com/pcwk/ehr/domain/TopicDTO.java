package com.pcwk.ehr.domain;

public class TopicDTO {
	private Integer topicNo	;//토픽 번호
	private String title	    ;//주제
	private String contents	    ;//주제 내용
	private String regId	    ;//작성자
	private String regDt	    ;//등록일
	private String modId	    ;//수정자
	private String modDt	    ;//수정일
	private Integer topicRatio	;//토픽별 기사 수 집계(%)
	
	public TopicDTO() {
		super();
	}

	public TopicDTO(Integer topicNo, String title, String contents, String regId, String regDt, String modId,
			String modDt, Integer topicRatio) {
		super();
		this.topicNo = topicNo;
		this.title = title;
		this.contents = contents;
		this.regId = regId;
		this.regDt = regDt;
		this.modId = modId;
		this.modDt = modDt;
		this.topicRatio = topicRatio;
	}

	public Integer getTopicNo() {
		return topicNo;
	}

	public void setTopicNo(Integer topicNo) {
		this.topicNo = topicNo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getRegId() {
		return regId;
	}

	public void setRegId(String regId) {
		this.regId = regId;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	public String getModId() {
		return modId;
	}

	public void setModId(String modId) {
		this.modId = modId;
	}

	public String getModDt() {
		return modDt;
	}

	public void setModDt(String modDt) {
		this.modDt = modDt;
	}

	public Integer getTopicRatio() {
		return topicRatio;
	}

	public void setTopicRatio(Integer topicRatio) {
		this.topicRatio = topicRatio;
	}

	@Override
	public String toString() {
		return "TopicDTO [topicNo=" + topicNo + ", title=" + title + ", contents=" + contents + ", regId=" + regId
				+ ", regDt=" + regDt + ", modId=" + modId + ", modDt=" + modDt + ", topicRatio=" + topicRatio
				+ ", toString()=" + super.toString() + "]";
	}

	
}
