package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper mapper;

	@Override
	public int insertUser(UserDTO param) {
		return mapper.insertUser(param);
	}

	@Override
	public UserDTO selectUser(int userNo) {
		return mapper.selectUser(userNo);
	}

	@Override
	public int deleteUser(int userNo) {
		return mapper.deleteUser(userNo);
	}

	@Override
	public String findPw(String email) {
		return mapper.findPw(email);
	}

	//로그인
	@Override
	public int login(UserDTO param) {
		//flag = 10 -> email 불일치
		//flag = 20 -> 비밀번호 불일치
		//flag = 30 -> email/비밀번호 일치
		int flag = 30;
		
		// 존재하는 이메일인지 확인
		if(mapper.checkEmail(param.getEmail())==0) {
			flag = 10;
			
			return flag;
		}
		// 이메일과 비밀번호가 일치하는지 확인
		if(mapper.checkPw(param)==0) {
			flag = 20;
			
			return flag;
		}
		// email/비밀번호 일치 로그인
		return flag;
	}

	@Override
	public int updateUser(UserDTO param) {
		return mapper.updateUser(param);
	}

	@Override
	public int updateImage(UserDTO param) {
		return mapper.updateUser(param);
	}

	@Override
	public List<UserDTO> userList(SearchDTO param) {
		return mapper.userList(param);
	}

	@Override
	public int updateRole(UserDTO param) {
		return mapper.updateRole(param);
	}

}
