package com.pcwk.ehr.domain;

import com.pcwk.ehr.cmn.DTO;

public class Shelter extends DTO {
	private Integer shelterNo    ;	//대피소 번호
	private String  reareNm      ;	//시설명
	private String  ronaDaddr    ;	//도로명 주소
	private double  lat          ;	//위도
	private double  lon          ;	//경도
	private int  shltCd          ; 	//대피소 구분 코드
	private String  shltNm       ;	//대피소 구분명
	
	public Shelter() {}

	public Shelter(Integer shelterNo, String reareNm, String ronaDaddr, double lat, double lon, int shltCd,
			String shltNm) {
		super();
		this.shelterNo = shelterNo;
		this.reareNm = reareNm;
		this.ronaDaddr = ronaDaddr;
		this.lat = lat;
		this.lon = lon;
		this.shltCd = shltCd;
		this.shltNm = shltNm;
	}

	/**
	 * @return the shelterNo
	 */
	public Integer getShelterNo() {
		return shelterNo;
	}

	/**
	 * @param shelterNo the shelterNo to set
	 */
	public void setShelterNo(Integer shelterNo) {
		this.shelterNo = shelterNo;
	}

	/**
	 * @return the reareNm
	 */
	public String getReareNm() {
		return reareNm;
	}

	/**
	 * @param reareNm the reareNm to set
	 */
	public void setReareNm(String reareNm) {
		this.reareNm = reareNm;
	}

	/**
	 * @return the ronaDaddr
	 */
	public String getRonaDaddr() {
		return ronaDaddr;
	}

	/**
	 * @param ronaDaddr the ronaDaddr to set
	 */
	public void setRonaDaddr(String ronaDaddr) {
		this.ronaDaddr = ronaDaddr;
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
	 * @return the shltCd
	 */
	public int getShltCd() {
		return shltCd;
	}

	/**
	 * @param shltCd the shltCd to set
	 */
	public void setShltCd(int shltCd) {
		this.shltCd = shltCd;
	}

	/**
	 * @return the shltNm
	 */
	public String getShltNm() {
		return shltNm;
	}

	/**
	 * @param shltNm the shltNm to set
	 */
	public void setShltNm(String shltNm) {
		this.shltNm = shltNm;
	}

	@Override
	public String toString() {
		return "Shelter [shelterNo=" + shelterNo + ", reareNm=" + reareNm + ", ronaDaddr=" + ronaDaddr + ", lat=" + lat
				+ ", lon=" + lon + ", shltCd=" + shltCd + ", shltNm=" + shltNm + "]";
	}

}
