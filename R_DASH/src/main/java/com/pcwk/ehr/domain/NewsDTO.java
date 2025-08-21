package com.pcwk.ehr.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NewsDTO {
	private Integer newsNo	;//뉴스 번호
	private String keyword	;//키워드 구분
	private String title    ;//뉴스 제목
	private String url      ;//
	private String company	;//신문사
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private String pubDt    ;//발행일
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private String regDt	;//저장일

	public NewsDTO() {
		super();
	}
	public NewsDTO(Integer newsNo, String keyword, String title, String url, String company, String pubDt,
			String regDt) {
		super();
		this.newsNo = newsNo;
		this.keyword = keyword;
		this.title = title;
		this.url = url;
		this.company = company;
		this.pubDt = pubDt;
		this.regDt = regDt;
	}
	public Integer getNewsNo() {
		return newsNo;
	}
	public void setNewsNo(Integer newsNo) {
		this.newsNo = newsNo;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPubDt() {
		return pubDt;
	}
	public void setPubDt(String pubDt) {
		this.pubDt = pubDt;
	}
	public String getRegDt() {
		return regDt;
	}
	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}
	@Override
	public String toString() {
		return "NewsDTO [newsNo=" + newsNo + ", keyword=" + keyword + ", title=" + title + ", url=" + url + ", company="
				+ company + ", pubDt=" + pubDt + ", regDt=" + regDt + "]";
	}
	
}
