package com.pcwk.ehr.fire.station;

public class FireStationDTO {

	private int fireNo; // 소방서 번호 (PK)
	private String fireName; // 소방서 이름
	private String area; // 지역
	private String address; // 상세 주소
	private String fireHead; // 소방서 본부명
	private String tel; // 전화 번호
	private double lat; // x좌표
	private double lon; // y좌표
	private String fireTp; // 유형

	/**
	 * 
	 */
	public FireStationDTO() {

	}

	/**
	 * @param fireNo
	 * @param fireName
	 * @param area
	 * @param address
	 * @param fireHead
	 * @param tel
	 * @param lat
	 * @param lon
	 * @param fireTp
	 */
	public FireStationDTO(int fireNo, String fireName, String area, String address, String fireHead, String tel,
			double lat, double lon, String fireTp) {
		super();
		this.fireNo = fireNo;
		this.fireName = fireName;
		this.area = area;
		this.address = address;
		this.fireHead = fireHead;
		this.tel = tel;
		this.lat = lat;
		this.lon = lon;
		this.fireTp = fireTp;
	}

	/**
	 * @return the fireNo
	 */
	public int getFireNo() {
		return fireNo;
	}

	/**
	 * @param fireNo the fireNo to set
	 */
	public void setFireNo(int fireNo) {
		this.fireNo = fireNo;
	}

	/**
	 * @return the fireName
	 */
	public String getFireName() {
		return fireName;
	}

	/**
	 * @param fireName the fireName to set
	 */
	public void setFireName(String fireName) {
		this.fireName = fireName;
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
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the fireHead
	 */
	public String getFireHead() {
		return fireHead;
	}

	/**
	 * @param fireHead the fireHead to set
	 */
	public void setFireHead(String fireHead) {
		this.fireHead = fireHead;
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
		return "FireStationDTO [fireNo=" + fireNo + ", fireName=" + fireName + ", area=" + area + ", address=" + address
				+ ", fireHead=" + fireHead + ", tel=" + tel + ", lat=" + lat + ", lon=" + lon + ", fireTp=" + fireTp
				+ "]";
	}

}
