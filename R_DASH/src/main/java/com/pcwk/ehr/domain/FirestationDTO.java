package com.pcwk.ehr.domain;

import com.pcwk.ehr.cmn.DTO;

public class FirestationDTO extends DTO {
	private Integer stationNo    ;
	private String  stationNm    ;
	private String  area         ;
	private String  fireNead     ;
	private String  tel          ;
	private double  lat          ;
	private double  lon          ;
	private String  fireTp       ;
	
	
	public FirestationDTO() {}

	public FirestationDTO(Integer stationNo, String stationNm, String area, String fireNead, String tel, double lat,
			double lon, String fireTp) {
		super();
		this.stationNo = stationNo;
		this.stationNm = stationNm;
		this.area = area;
		this.fireNead = fireNead;
		this.tel = tel;
		this.lat = lat;
		this.lon = lon;
		this.fireTp = fireTp;
	}

	/**
	 * @return the stationNo
	 */
	public Integer getStationNo() {
		return stationNo;
	}

	/**
	 * @param stationNo the stationNo to set
	 */
	public void setStationNo(Integer stationNo) {
		this.stationNo = stationNo;
	}

	/**
	 * @return the stationNm
	 */
	public String getStationNm() {
		return stationNm;
	}

	/**
	 * @param stationNm the stationNm to set
	 */
	public void setStationNm(String stationNm) {
		this.stationNm = stationNm;
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
	 * @return the fireNead
	 */
	public String getFireNead() {
		return fireNead;
	}

	/**
	 * @param fireNead the fireNead to set
	 */
	public void setFireNead(String fireNead) {
		this.fireNead = fireNead;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
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

	@Override
	public String toString() {
		return "FireStation [stationNo=" + stationNo + ", stationNm=" + stationNm + ", area=" + area + ", fireNead="
				+ fireNead + ", tel=" + tel + ", lat=" + lat + ", lon=" + lon + ", fireTp=" + fireTp + "]";
	}
	
	
}
