package com.pcwk.ehr.domain;

public class FireDisasterDTO {

	private Long disasterNo; // 화재순번 (PK)

	private String fireDate; // 일시

	private String area; // 시도

	private String areaDp; // 시군구 구분

	private String fireTp; // 화재유형 구분

	private String utLg; // 발화열원 대분류

	private String cutLg; // 발화요인 대분류

	private String firstLg; // 최초 착화물 대분류

	private Integer totalCa; // 인명피해 합계

	private Integer deathTl; // 사망자 수

	private Integer injurNm; // 부상자 수

	private Long comDm; // 재산피해 소계

	private String placeLg; // 장소 대분류

	/**
	 * @param disasterNo
	 * @param fireDate
	 * @param area
	 * @param areaDp
	 * @param fireTp
	 * @param utLg
	 * @param cutLg
	 * @param firstLg
	 * @param totalCa
	 * @param deathTl
	 * @param injurNm
	 * @param comDm
	 * @param placeLg
	 */
	public FireDisasterDTO(Long disasterNo, String fireDate, String area, String areaDp, String fireTp, String utLg,
			String cutLg, String firstLg, Integer totalCa, Integer deathTl, Integer injurNm, Long comDm,
			String placeLg) {
		super();
		this.disasterNo = disasterNo;
		this.fireDate = fireDate;
		this.area = area;
		this.areaDp = areaDp;
		this.fireTp = fireTp;
		this.utLg = utLg;
		this.cutLg = cutLg;
		this.firstLg = firstLg;
		this.totalCa = totalCa;
		this.deathTl = deathTl;
		this.injurNm = injurNm;
		this.comDm = comDm;
		this.placeLg = placeLg;
	}

	/**
	 * @return the disasterNo
	 */
	public Long getDisasterNo() {
		return disasterNo;
	}

	/**
	 * @param disasterNo the disasterNo to set
	 */
	public void setDisasterNo(Long disasterNo) {
		this.disasterNo = disasterNo;
	}

	/**
	 * @return the fireDate
	 */
	public String getFireDate() {
		return fireDate;
	}

	/**
	 * @param fireDate the fireDate to set
	 */
	public void setFireDate(String fireDate) {
		this.fireDate = fireDate;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the areaDp
	 */
	public String getAreaDp() {
		return areaDp;
	}

	/**
	 * @param areaDp the areaDp to set
	 */
	public void setAreaDp(String areaDp) {
		this.areaDp = areaDp;
	}

	/**
	 * @return the fireTp
	 */
	public String getFireTp() {
		return fireTp;
	}

	/**
	 * @param fireTp the fireTp to set
	 */
	public void setFireTp(String fireTp) {
		this.fireTp = fireTp;
	}

	/**
	 * @return the utLg
	 */
	public String getUtLg() {
		return utLg;
	}

	/**
	 * @param utLg the utLg to set
	 */
	public void setUtLg(String utLg) {
		this.utLg = utLg;
	}

	/**
	 * @return the cutLg
	 */
	public String getCutLg() {
		return cutLg;
	}

	/**
	 * @param cutLg the cutLg to set
	 */
	public void setCutLg(String cutLg) {
		this.cutLg = cutLg;
	}

	/**
	 * @return the firstLg
	 */
	public String getFirstLg() {
		return firstLg;
	}

	/**
	 * @param firstLg the firstLg to set
	 */
	public void setFirstLg(String firstLg) {
		this.firstLg = firstLg;
	}

	/**
	 * @return the totalCa
	 */
	public Integer getTotalCa() {
		return totalCa;
	}

	/**
	 * @param totalCa the totalCa to set
	 */
	public void setTotalCa(Integer totalCa) {
		this.totalCa = totalCa;
	}

	/**
	 * @return the deathTl
	 */
	public Integer getDeathTl() {
		return deathTl;
	}

	/**
	 * @param deathTl the deathTl to set
	 */
	public void setDeathTl(Integer deathTl) {
		this.deathTl = deathTl;
	}

	/**
	 * @return the injurNm
	 */
	public Integer getInjurNm() {
		return injurNm;
	}

	/**
	 * @param injurNm the injurNm to set
	 */
	public void setInjurNm(Integer injurNm) {
		this.injurNm = injurNm;
	}

	/**
	 * @return the comDm
	 */
	public Long getComDm() {
		return comDm;
	}

	/**
	 * @param comDm the comDm to set
	 */
	public void setComDm(Long comDm) {
		this.comDm = comDm;
	}

	/**
	 * @return the placeLg
	 */
	public String getPlaceLg() {
		return placeLg;
	}

	/**
	 * @param placeLg the placeLg to set
	 */
	public void setPlaceLg(String placeLg) {
		this.placeLg = placeLg;
	}

	@Override
	public String toString() {
		return "FireDisasterDTO [disasterNo=" + disasterNo + ", fireDate=" + fireDate + ", area=" + area + ", areaDp="
				+ areaDp + ", fireTp=" + fireTp + ", utLg=" + utLg + ", cutLg=" + cutLg + ", firstLg=" + firstLg
				+ ", totalCa=" + totalCa + ", deathTl=" + deathTl + ", injurNm=" + injurNm + ", comDm=" + comDm
				+ ", placeLg=" + placeLg + "]";
	}

}
