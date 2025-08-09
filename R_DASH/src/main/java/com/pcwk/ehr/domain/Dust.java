package com.pcwk.ehr.domain;

import com.pcwk.ehr.cmn.DTO;

public class Dust extends DTO {
	private Integer dustNo ;	//고유 번호
	private String 	tm     ;	//관측 시간
	private String 	stnNm  ;	//지점명
	private double 	lat    ;	//위도
	private double 	lon    ;	//경도
	private String 	org    ;	//관측기관
	private int 	pm10   ;	//PM10 관측값
	private int 	avg    ;	//PM10 시간평균값
	private int 	max    ;	//PM10 시간최대값
	private int 	cnt    ;	//PM10 자료갯수
	
	
	public Dust() {}


	public Dust(Integer dustNo, String tm, String stnNm, double lat, double lon, String org, int pm10, int avg, int max,
			int cnt) {
		super();
		this.dustNo = dustNo;
		this.tm = tm;
		this.stnNm = stnNm;
		this.lat = lat;
		this.lon = lon;
		this.org = org;
		this.pm10 = pm10;
		this.avg = avg;
		this.max = max;
		this.cnt = cnt;
	}


	/**
	 * @return the dustNo
	 */
	public Integer getDustNo() {
		return dustNo;
	}

	/**
	 * @param dustNo the dustNo to set
	 */
	public void setDustNo(Integer dustNo) {
		this.dustNo = dustNo;
	}

	/**
	 * @return the tm
	 */
	public String getTm() {
		return tm;
	}

	/**
	 * @param tm the tm to set
	 */
	public void setTm(String tm) {
		this.tm = tm;
	}

	/**
	 * @return the stnNm
	 */
	public String getStnNm() {
		return stnNm;
	}

	/**
	 * @param stnNm the stnNm to set
	 */
	public void setStnNm(String stnNm) {
		this.stnNm = stnNm;
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
	 * @return the org
	 */
	public String getOrg() {
		return org;
	}

	/**
	 * @param org the org to set
	 */
	public void setOrg(String org) {
		this.org = org;
	}

	/**
	 * @return the pm10
	 */
	public int getPm10() {
		return pm10;
	}

	/**
	 * @param pm10 the pm10 to set
	 */
	public void setPm10(int pm10) {
		this.pm10 = pm10;
	}

	/**
	 * @return the avg
	 */
	public int getAvg() {
		return avg;
	}

	/**
	 * @param avg the avg to set
	 */
	public void setAvg(int avg) {
		this.avg = avg;
	}

	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * @return the cnt
	 */
	public int getCnt() {
		return cnt;
	}

	/**
	 * @param cnt the cnt to set
	 */
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	@Override
	public String toString() {
		return "Dust [dustNo=" + dustNo + ", tm=" + tm + ", stnNm=" + stnNm + ", lat=" + lat + ", lon=" + lon + ", org="
				+ org + ", pm10=" + pm10 + ", avg=" + avg + ", max=" + max + ", cnt=" + cnt + "]";
	}
	
	
	
}
