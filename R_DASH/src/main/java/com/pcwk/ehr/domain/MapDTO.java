package com.pcwk.ehr.domain;

import java.sql.Timestamp;

import com.pcwk.ehr.cmn.DTO;

public class MapDTO extends DTO {
	private Integer viewNo; // VIEW_NO : 지도 고유 번호(뷰 PK)
	private String tableNm; // TABLE_NM : 원본 테이블 이름(= 재난 코드로도 사용)
	private Integer targetNo; // TARGET_NO : 원본 테이블 PK
	private Double lat; // LAT
	private Double lon; // LON
	private java.sql.Timestamp occurDt; // OCCUR_DT (null 허용) /DB에서 OCCUR_DT가 DATE 타입 / 오라클의 DATE는 시·분·초까지 저장
	private String title; // TITLE : 마커 제목
	private String description; // DESCRIPTION : 알림판 내용
	private String dangerLevel; // DANGER_LEVEL : 등급 코드(ENUM 권장)
	private String iconUrl; // ICON_URL
	private String colorCd; // COLOR_CD : "#RRGGBB"
	private String visualType; // VISUAL_TYPE : 마커/히트맵/폴리라인 등

	public MapDTO() {
	}

	/**
	 * @param viewNo
	 * @param tableNm
	 * @param targetNo
	 * @param lat
	 * @param lon
	 * @param occurDt
	 * @param title
	 * @param description
	 * @param dangerLevel
	 * @param iconUrl
	 * @param colorCd
	 * @param visualType
	 */
	public MapDTO(Integer viewNo, String tableNm, Integer targetNo, Double lat, Double lon, Timestamp occurDt,
			String title, String description, String dangerLevel, String iconUrl, String colorCd, String visualType) {
		super();
		this.viewNo = viewNo;
		this.tableNm = tableNm;
		this.targetNo = targetNo;
		this.lat = lat;
		this.lon = lon;
		this.occurDt = occurDt;
		this.title = title;
		this.description = description;
		this.dangerLevel = dangerLevel;
		this.iconUrl = iconUrl;
		this.colorCd = colorCd;
		this.visualType = visualType;
	}

	/**
	 * @return the viewNo
	 */
	public Integer getViewNo() {
		return viewNo;
	}

	/**
	 * @param viewNo the viewNo to set
	 */
	public void setViewNo(Integer viewNo) {
		this.viewNo = viewNo;
	}

	/**
	 * @return the tableNm
	 */
	public String getTableNm() {
		return tableNm;
	}

	/**
	 * @param tableNm the tableNm to set
	 */
	public void setTableNm(String tableNm) {
		this.tableNm = tableNm;
	}

	/**
	 * @return the targetNo
	 */
	public Integer getTargetNo() {
		return targetNo;
	}

	/**
	 * @param targetNo the targetNo to set
	 */
	public void setTargetNo(Integer targetNo) {
		this.targetNo = targetNo;
	}

	/**
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lon
	 */
	public Double getLon() {
		return lon;
	}

	/**
	 * @param lon the lon to set
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * @return the occurDt
	 */
	public java.sql.Timestamp getOccurDt() {
		return occurDt;
	}

	/**
	 * @param occurDt the occurDt to set
	 */
	public void setOccurDt(java.sql.Timestamp occurDt) {
		this.occurDt = occurDt;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the dangerLevel
	 */
	public String getDangerLevel() {
		return dangerLevel;
	}

	/**
	 * @param dangerLevel the dangerLevel to set
	 */
	public void setDangerLevel(String dangerLevel) {
		this.dangerLevel = dangerLevel;
	}

	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * @param iconUrl the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * @return the colorCd
	 */
	public String getColorCd() {
		return colorCd;
	}

	/**
	 * @param colorCd the colorCd to set
	 */
	public void setColorCd(String colorCd) {
		this.colorCd = colorCd;
	}

	/**
	 * @return the visualType
	 */
	public String getVisualType() {
		return visualType;
	}

	/**
	 * @param visualType the visualType to set
	 */
	public void setVisualType(String visualType) {
		this.visualType = visualType;
	}

	@Override
	public String toString() {
		return "MapDTO [viewNo=" + viewNo + ", tableNm=" + tableNm + ", targetNo=" + targetNo + ", lat=" + lat
				+ ", lon=" + lon + ", occurDt=" + occurDt + ", title=" + title + ", description=" + description
				+ ", dangerLevel=" + dangerLevel + ", iconUrl=" + iconUrl + ", colorCd=" + colorCd + ", visualType="
				+ visualType + "]";
	}

}
