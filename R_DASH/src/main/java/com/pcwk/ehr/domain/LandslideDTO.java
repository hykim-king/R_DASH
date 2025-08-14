package com.pcwk.ehr.domain;

import com.pcwk.ehr.cmn.DTO;

public class LandslideDTO extends DTO {
	private int lndApntCd    ;	//산사태 현황 구분번호  1= 경보 / 2 = 주의보
	private String  lndApntNm    ;	//산사태 현황
	private String  lndApntStts  ;	//예보발령상태
	private String  lndApntDt    ;	//발생 일시
	private String  lndInstNm    ;	//발생 지역
	private double  lat          ;	//위도
	private double  lon          ;	//경도
	private Integer landslideNo  ;  //산사태 구분 번호
	
	public LandslideDTO() {
	}


	public LandslideDTO(int lndApntCd, String lndApntNm, String lndApntStts, String lndApntDt, String lndInstNm,
			double lat, double lon, Integer landslideNo) {
		super();
		this.lndApntCd = lndApntCd;
		this.lndApntNm = lndApntNm;
		this.lndApntStts = lndApntStts;
		this.lndApntDt = lndApntDt;
		this.lndInstNm = lndInstNm;
		this.lat = lat;
		this.lon = lon;
		this.landslideNo = landslideNo;
	}

	/**
	 * @return the lndApntCd
	 */
	public int getLndApntCd() {
		return lndApntCd;
	}

	/**
	 * @param lndApntCd the lndApntCd to set
	 */
	public void setLndApntCd(int lndApntCd) {
		this.lndApntCd = lndApntCd;
	}

	/**
	 * @return the lndApntNm
	 */
	public String getLndApntNm() {
		return lndApntNm;
	}

	/**
	 * @param lndApntNm the lndApntNm to set
	 */
	public void setLndApntNm(String lndApntNm) {
		this.lndApntNm = lndApntNm;
	}

	/**
	 * @return the lndApntStts
	 */
	public String getLndApntStts() {
		return lndApntStts;
	}

	/**
	 * @param lndApntStts the lndApntStts to set
	 */
	public void setLndApntStts(String lndApntStts) {
		this.lndApntStts = lndApntStts;
	}

	/**
	 * @return the lndApntDt
	 */
	public String getLndApntDt() {
		return lndApntDt;
	}

	/**
	 * @param lndApntDt the lndApntDt to set
	 */
	public void setLndApntDt(String lndApntDt) {
		this.lndApntDt = lndApntDt;
	}

	/**
	 * @return the lndInstNm
	 */
	public String getLndInstNm() {
		return lndInstNm;
	}

	/**
	 * @param lndInstNm the lndInstNm to set
	 */
	public void setLndInstNm(String lndInstNm) {
		this.lndInstNm = lndInstNm;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}

	/**
	 * @return the landslideNo
	 */
	public Integer getLandslideNo() {
		return landslideNo;
	}

	/**
	 * @param landslideNo the landslideNo to set
	 */
	public void setLandslideNo(Integer landslideNo) {
		this.landslideNo = landslideNo;
	}

	@Override
	public String toString() {
		return "Landslide [lndApntCd=" + lndApntCd + ", lndApntNm=" + lndApntNm + ", lndApntStts=" + lndApntStts
				+ ", lndApntDt=" + lndApntDt + ", lndInstNm=" + lndInstNm + ", lat=" + lat + ", lon=" + lon
				+ ", landslideNo=" + landslideNo + "]";
	}

}
