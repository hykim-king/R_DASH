package com.pcwk.ehr.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.UserMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
class UserControllerTest {
	Logger log = LogManager.getLogger(getClass());

	// 웹브라우저 대역 객체
	MockMvc mockMvc;

	UserDTO dto01;

	@Autowired
	UserMapper mapper;
	
	@Autowired
	WebApplicationContext webApplicationContext;

	@BeforeEach
	void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		dto01 = new UserDTO(1, "1234", "이상무1", "부리부리1", "이미지", "pcwkkk1@gmail.com", "010-1234-5678", 12345, "z", "z", 1,
				"z");
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void login() {
		
	}
	@Disabled
	@Test
	void beans() {
		assertNotNull(mockMvc);
		assertNotNull(webApplicationContext);
		assertNotNull(mapper);
		
		log.debug("mockMvc:{}",mockMvc);
		log.debug("webApplicationContext:{}",webApplicationContext);
		log.debug("mapper:{}",mapper);
	}

}
