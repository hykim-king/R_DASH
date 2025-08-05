package com.pcwk.ehr.domain;

import com.pcwk.ehr.cmn.DTO;

public class UserDTO extends DTO{
	private String userNo; // 회원번호
	private String password; // 비밀번호
	private String name; // 이름
	private String nickname; // 닉네임
	private String image; // 이미지
	private String email; // 이메일
	private String tel; // 전화번호
	private String zip_code; // 우편번호
	private String address; // 주소
	private String detailAddress; // 상세 주소
	private String role; // 권한 구분
	private String regDt; // 등록일

	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserDTO(String userNo, String password, String name, String nickname, String image, String email, String tel,
			String zip_code, String address, String detailAddress, String role, String regDt) {
		super();
		this.userNo = userNo;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.image = image;
		this.email = email;
		this.tel = tel;
		this.zip_code = zip_code;
		this.address = address;
		this.detailAddress = detailAddress;
		this.role = role;
		this.regDt = regDt;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getZip_code() {
		return zip_code;
	}

	public void setZip_code(String zip_code) {
		this.zip_code = zip_code;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRegDt() {
		return regDt;
	}

	public void setRegDt(String regDt) {
		this.regDt = regDt;
	}

	@Override
	public String toString() {
		return "UserDTO [userNo=" + userNo + ", password=" + password + ", name=" + name + ", nickname=" + nickname
				+ ", image=" + image + ", email=" + email + ", tel=" + tel + ", zip_code=" + zip_code + ", address="
				+ address + ", detailAddress=" + detailAddress + ", role=" + role + ", regDt=" + regDt + "]";
	}

}
