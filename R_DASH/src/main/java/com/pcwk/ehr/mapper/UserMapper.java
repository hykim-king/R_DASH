package com.pcwk.ehr.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.UserDTO;

@Mapper
public interface UserMapper{
	// 회원가입 O
	public int insertUser(UserDTO param);

	// 회원 조회 O 
	public UserDTO selectUser(int userNo);
	
	// 회원 삭제 O
	public int deleteUser(int userNo);

	// 회원 정보 수정 O
	public int updateUser(UserDTO param);

	// 이메일 확인(회원가입 시 중복체크, 비밀번호 찾기) O 
	public int checkEmail(String email);
	
	// 로그인 시 비밀번호 확인 O
	public int checkPw(UserDTO param);
	
	// 전체 회원 조회, 페이징(관리자)
	public UserDTO userList(SearchDTO param);

	// 회원 전체 삭제(test) O
	public int deleteAll();
	
	//다건 등록(test) O
	public int saveAll();
	
	// 전체 회원 수 조회 O
	public int getCount();
	
	//전체 회원 조회 O
	public UserDTO getAll();
	

	
}
