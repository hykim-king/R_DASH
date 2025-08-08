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

	@Override
	public int login(UserDTO param) {
		int flag = 0;
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
