package com.pcwk.ehr.domain;

import com.pcwk.ehr.cmn.DTO;

public class UserDTO extends DTO {
	private int userNo; // 회원번호
	private String social; //소셜 로그인 구분
	private String password; // 비밀번호
	private String name; // 이름
	private String nickname; // 닉네임
	private String image; // 이미지
	private String email; // 이메일
	private String tel; // 전화번호
	private Integer zipCode; // 우편번호
	private String address; // 주소
	private String detailAddress; // 상세 주소
	private int role; // 권한 구분
	private String regDt; // 등록일

	public UserDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDTO(int userNo, String password, String name, String nickname, String image, String email, String tel,
			Integer zipCode, String address, String detailAddress, int role, String regDt) {
		super();
		this.userNo = userNo;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.image = image;
		this.email = email;
		this.tel = tel;
		this.zipCode = zipCode;
		this.address = address;
		this.detailAddress = detailAddress;
		this.role = role;
		this.regDt = regDt;
	}

	public int getUserNo() {
		return userNo;
	}

	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}

	public String getSocial() {
		return social;
	}

	public void setSocial(String social) {
		this.social = social;
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

	public Integer getZipCode() {
		return zipCode;
	}

	public void setZipCode(Integer zipCode) {
		this.zipCode = zipCode;
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

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
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
		return "UserDTO [userNo=" + userNo + ", social=" + social + ", password=" + password + ", name=" + name
				+ ", nickname=" + nickname + ", image=" + image + ", email=" + email + ", tel=" + tel + ", zipCode="
				+ zipCode + ", address=" + address + ", detailAddress=" + detailAddress + ", role=" + role + ", regDt="
				+ regDt + "]";
	}





}
