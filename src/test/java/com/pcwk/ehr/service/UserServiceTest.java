package com.pcwk.ehr.service;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.UserMapper;
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"		
									,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"})
class UserServiceTest {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	UserService service;
	
	@Autowired
	UserMapper mapper;
	
	UserDTO dto01;
	
	@BeforeEach
	void setUp() throws Exception {
		dto01 = new UserDTO(1, "1234", "이상무1", "부리부리1", "이미지", "pcwkkk1@gmail.com", "010-1234-5678", 12345, "z", "z", 1,
				"z");
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void login() {
		//항상 같은 결과
		// 1. 전체 삭제
		// 2. 단건 등록
		// 3. 단건 조회
		// 4. 로그인 확인
		
		// 1.
		mapper.deleteAll();
		
		// 2.
		int flag = mapper.insertUser(dto01);
		assertEquals(1, flag);
		
		// 3.
		int userNo = mapper.getUserNo(dto01.getEmail());
		UserDTO user = mapper.selectUser(userNo);
		assertNotNull(user);
		
		// 4.1 이메일 비밀번호 일치
		flag = service.login(user);
		assertEquals(30, flag);
		
		// 4.2 비밀번호 불일치
		user.setPassword("2345");
		flag = service.login(user);
		assertEquals(20, flag);
		
		// 4.3 계정 없음
		user.setEmail("qweqwe@gmail.com");
		flag = service.login(user);
		assertEquals(10, flag);
		
		
		
	}
	

	@Disabled
	@Test
	void beans() {
		assertNotNull(context);
		assertNotNull(service);
		assertNotNull(mapper);
		
		log.debug("context:{}",context);
		log.debug("service:{}",service);
		log.debug("service:{}",mapper);
	}

}
