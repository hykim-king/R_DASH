package com.pcwk.ehr.domain;

public class NowcastDTO {

	private Integer nowcastNo;
	private String baseDate;
	private String baseTime;
	private String sidoNm;
	private String sigunguNm;
	private int nx;
	private int ny;
	private String category;
	private double obsrValue;

	public NowcastDTO() {
	}

	/**
	 * @return the nowcastNo
	 */
	public Integer getNowcastNo() {
		return nowcastNo;
	}

	/**
	 * @param nowcastNo the nowcastNo to set
	 */
	public void setNowcastNo(Integer nowcastNo) {
		this.nowcastNo = nowcastNo;
	}

	/**
	 * @return the baseDate
	 */
	public String getBaseDate() {
		return baseDate;
	}

	/**
	 * @param baseDate the baseDate to set
	 */
	public void setBaseDate(String baseDate) {
		this.baseDate = baseDate;
	}

	/**
	 * @return the baseTime
	 */
	public String getBaseTime() {
		return baseTime;
	}

	/**
	 * @param baseTime the baseTime to set
	 */
	public void setBaseTime(String baseTime) {
		this.baseTime = baseTime;
	}

	/**
	 * @return the sidoNm
	 */
	public String getSidoNm() {
		return sidoNm;
	}

	/**
	 * @param sidoNm the sidoNm to set
	 */
	public void setSidoNm(String sidoNm) {
		this.sidoNm = sidoNm;
	}

	/**
	 * @return the sigunguNm
	 */
	public String getSigunguNm() {
		return sigunguNm;
	}

	/**
	 * @param sigunguNm the sigunguNm to set
	 */
	public void setSigunguNm(String sigunguNm) {
		this.sigunguNm = sigunguNm;
	}

	/**
	 * @return the nx
	 */
	public int getNx() {
		return nx;
	}

	/**
	 * @param nx the nx to set
	 */
	public void setNx(int nx) {
		this.nx = nx;
	}

	/**
	 * @return the ny
	 */
	public int getNy() {
		return ny;
	}

	/**
	 * @param ny the ny to set
	 */
	public void setNy(int ny) {
		this.ny = ny;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the obsrValue
	 */
	public double getObsrValue() {
		return obsrValue;
	}

	/**
	 * @param obsrValue the obsrValue to set
	 */
	public void setObsrValue(double obsrValue) {
		this.obsrValue = obsrValue;
	}

	/**
	 * @param nowcastNo
	 * @param baseDate
	 * @param baseTime
	 * @param sidoNm
	 * @param sigunguNm
	 * @param nx
	 * @param ny
	 * @param category
	 * @param obsrValue
	 */
	public NowcastDTO(Integer nowcastNo, String baseDate, String baseTime, String sidoNm, String sigunguNm, int nx,
			int ny, String category, double obsrValue) {
		super();
		this.nowcastNo = nowcastNo;
		this.baseDate = baseDate;
		this.baseTime = baseTime;
		this.sidoNm = sidoNm;
		this.sigunguNm = sigunguNm;
		this.nx = nx;
		this.ny = ny;
		this.category = category;
		this.obsrValue = obsrValue;
	}

	@Override
	public String toString() {
		return "NowcastDTO [nowcastNo=" + nowcastNo + ", baseDate=" + baseDate + ", baseTime=" + baseTime + ", sidoNm="
				+ sidoNm + ", sigunguNm=" + sigunguNm + ", nx=" + nx + ", ny=" + ny + ", category=" + category
				+ ", obsrValue=" + obsrValue + "]";
	}
	
}
