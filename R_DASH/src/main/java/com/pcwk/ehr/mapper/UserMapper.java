package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.UserDTO;

@Mapper
public interface UserMapper{
	// 회원가입 OO
	public int insertUser(UserDTO param);

	// 회원 조회 OO
	public UserDTO selectUser(int userNo);
	
	// 회원 삭제 OO
	public int deleteUser(int userNo);
	
	// 비밀번호 확인 OO
	public String findPw(String email);

	// 회원 정보 수정 OO
	public int updateUser(UserDTO param);

	// 이메일 확인(회원가입 시 중복체크, 비밀번호 찾기) OO 
	public int checkEmail(String email);
	
	// 로그인 시 비밀번호 확인 OO
	public int checkPw(UserDTO param);
	
	// 전체 회원 조회, 페이징(관리자) OO
	public List<UserDTO> userList(SearchDTO param);

	// 회원 전체 삭제(test) OO
	public int deleteAll();
	
	//다건 등록(test) OO
	public int saveAll();
	
	//UserNo 얻기(test) OO
	public int getUserNo(String email);
	
	// 전체 회원 수 조회 OO
	public int getCount();
	
	//전체 회원 조회 OO
	public List<UserDTO> getAll();
	
	//권한 변경 OO
	public int updateRole(UserDTO param);
	

	
}
