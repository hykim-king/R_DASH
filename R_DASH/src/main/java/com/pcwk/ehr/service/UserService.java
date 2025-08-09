package com.pcwk.ehr.service;

import java.util.List;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.UserDTO;

public interface UserService {
	// 회원가입
	public int insertUser(UserDTO param);

	// 회원 조회
	public UserDTO selectUser(int userNo);

	// 회원 탈퇴
	public int deleteUser(int userNo);

	// 비밀번호 찾기
	public String findPw(String email);

	// 로그인
	public int login(UserDTO param);

	// 회원 정보 수정
	public int updateUser(UserDTO param);

	// 프로필 사진 수정
	public int updateImage(UserDTO param);

	// 회원 조회 (페이징)
	public List<UserDTO> userList(SearchDTO param);
	
	// 권한 변경
	public int updateRole(UserDTO param);
}
