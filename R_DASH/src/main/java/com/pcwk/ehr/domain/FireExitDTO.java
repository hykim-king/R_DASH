package com.pcwk.ehr.domain;

public class FireExitDTO {

	private Long exitNo; // 소화기 번호 (PK)

	private String subNo; // 지하철 호선

	private String subName; // 지하철 역명

	private String udGr; // 지상/지하 구분

	private String locPl; // 역 층 구분

	/**
	 * @param exitNo
	 * @param subNo
	 * @param subName
	 * @param udGr
	 * @param locPl
	 */
	public FireExitDTO(Long exitNo, String subNo, String subName, String udGr, String locPl) {
		super();
		this.exitNo = exitNo;
		this.subNo = subNo;
		this.subName = subName;
		this.udGr = udGr;
		this.locPl = locPl;
	}

	/**
	 * @return the exitNo
	 */
	public Long getExitNo() {
		return exitNo;
	}

	/**
	 * @param exitNo the exitNo to set
	 */
	public void setExitNo(Long exitNo) {
		this.exitNo = exitNo;
	}

	/**
	 * @return the subNo
	 */
	public String getSubNo() {
		return subNo;
	}

	/**
	 * @param subNo the subNo to set
	 */
	public void setSubNo(String subNo) {
		this.subNo = subNo;
	}

	/**
	 * @return the subName
	 */
	public String getSubName() {
		return subName;
	}

	/**
	 * @param subName the subName to set
	 */
	public void setSubName(String subName) {
		this.subName = subName;
	}

	/**
	 * @return the udGr
	 */
	public String getUdGr() {
		return udGr;
	}

	/**
	 * @param udGr the udGr to set
	 */
	public void setUdGr(String udGr) {
		this.udGr = udGr;
	}

	/**
	 * @return the locPl
	 */
	public String getLocPl() {
		return locPl;
	}

	/**
	 * @param locPl the locPl to set
	 */
	public void setLocPl(String locPl) {
		this.locPl = locPl;
	}

	@Override
	public String toString() {
		return "FireExitDTO [exitNo=" + exitNo + ", subNo=" + subNo + ", subName=" + subName + ", udGr=" + udGr
				+ ", locPl=" + locPl + "]";
	}

}
