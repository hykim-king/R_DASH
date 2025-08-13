package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pcwk.ehr.cmn.FileUtil;
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
	public int deleteUser(UserDTO param) {	
		int flag = mapper.checkPw(param);
		if(flag == 1) {
			mapper.deleteUser(param.getUserNo());
		}		
		return flag;
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
	public int updateUserInfo(UserDTO param) {
		return mapper.updateUser(param);
	}

	@Override
	public List<UserDTO> userList(SearchDTO param) {
		return mapper.userList(param);
	}

	@Override
	public int updateRole(UserDTO param) {
		UserDTO user = mapper.selectUser(param.getUserNo());
		int flag = 0;
		
		//관리자를 일반회원으로
		if(user.getRole()==1) {
			user.setRole(2);
			mapper.updateRole(user);
			flag = 1;
		//일반회원을 관리자로
		}else {
			user.setRole(1);
			mapper.updateRole(user);
			flag = 2;
		}
		return flag;
	}

	@Override
	public int checkEmail(String email) {
		return mapper.checkEmail(email);
	}

	@Override
	public int updateUserImage(UserDTO param, MultipartFile file) {
		int flag = 0;
		
		
		if (file != null && !file.isEmpty()) {
			// 기존 이미지 파일 삭제
			UserDTO user = mapper.selectUser(param.getUserNo());
			if (user.getImage() != null && user.getImage() != "defaultProfile.jpg") {

                String existingFilePath = "C:/Users/user/R_DASH/R_DASH/src/main/webapp/resources/image/profile/" + user.getImage();
                FileUtil.deleteFile(existingFilePath);

            }
			// 이미지 저장
			String uploadDir = "C:/Users/user/R_DASH/R_DASH/src/main/webapp/resources/image/profile";
			String savedFilename = FileUtil.saveFileWithUUID(file, uploadDir);
			
			param.setImage(savedFilename);
			
			flag = mapper.updateUser(param);
			
		}
		return flag;
	}

}
