package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.UserDTO;

public interface UserService {
	// 회원가입
	public int insertUser(UserDTO param);

	// 회원 조회
	public UserDTO selectUser(int userNo);

	// 회원 탈퇴
	public int deleteUser(UserDTO param);

	// 비밀번호 찾기
	public String findPw(String email);

	// 로그인
	public int login(UserDTO param);

	// 회원 정보 수정
	public int updateUserInfo(UserDTO param);
	
	// 회원 이미지 수정
	public int updateUserImage(UserDTO param, MultipartFile file);

	// 회원 조회 (페이징)
	public List<UserDTO> userList(SearchDTO param);
	
	// 권한 변경
	public int updateRole(UserDTO param);
	
	// 이메일 중복 확인
	public int checkEmail(String email);
}
