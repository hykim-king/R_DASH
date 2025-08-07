package com.pcwk.ehr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import com.pcwk.ehr.domain.TopicDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.TopicMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"		
									,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"})

class NewsServiceTest {
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	TopicMapper topicMapper;
	
	@Autowired
	NewsServiceImpl service;
	
	TopicDTO topicDto;
	
	Logger log = LogManager.getLogger(getClass());
	
	
	//1. 자바에서 등록 시 어드민으로 등록되는지 확인
	//2. 업데이트 시 어드민으로 등록되는지 확인
	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
		
		topicDto = new TopicDTO(0, "주제1", "주제내용1", "AI", "사용안함", null, "사용안함", 4.23);
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}
	/**
	 * 로그인 0,  ADMIN 0 => 업데이트 가능
	 */
	@Test
	void doUpdateAdmin() {
		//1. 전체 삭제
		topicMapper.deleteAll();
		assertEquals(0,topicMapper.getCount());
		
		//2. 단 건 등록
		topicMapper.doSave(topicDto);
		assertEquals(1,topicMapper.getCount());
		
		//3. 세션 설정
		UserDTO user = new UserDTO();
		
		user.setNickname("ADMIN");
		user.setRole(1);
		
		service.session.setAttribute("user", user);
		
		//4. 조회
		TopicDTO outVO = topicMapper.doSelectOne(topicDto);
		
		//5. 업데이트
		outVO.setTitle("주제 수정됨");
		outVO.setContents("주제 내용 수정 됨.");
		
		int flag = service.doUpdate(outVO);
		log.debug("flag: {}"+flag);
		
		//6. 업데이트 조회
		TopicDTO inVO = topicMapper.doSelectOne(outVO);
		log.debug("inVO: {}"+inVO);
		
		//7. 수정자 확인
		assertEquals("ADMIN", inVO.getModId());
		
	}
	@Test
	void doSaveAdmin() {
		//1. 
		topicMapper.deleteAll();
		assertEquals(0,topicMapper.getCount());
		//2. 세션 설정
		UserDTO user = new UserDTO();
		
		user.setNickname("ADMIN");
		user.setRole(1);
		
		service.session.setAttribute("user", user);
		//3. 저장
		int flag = service.doSave(topicDto);
		log.debug("flag: {}"+flag);
		//4. 조회
		TopicDTO outVO = topicMapper.doSelectOne(topicDto);
		log.debug("outVO: {}"+outVO);
		assertEquals("ADMIN", outVO.getRegId());
		assertEquals("ADMIN", outVO.getModId());
		
	}
	
	//@Disabled
	@Test
	void UserIsNotAdminThrowException() {
		//1. 
		topicMapper.deleteAll();
		assertEquals(0,topicMapper.getCount());
		
		UserDTO user = new UserDTO();
		
		user.setNickname("user");
		user.setRole(2);
		
		service.session.setAttribute("user", user);
		
		//service.doSave(topicDto); // 관리자 권한 필요
		Exception e = assertThrows(RuntimeException.class,()->{
			service.doSave(topicDto);
		});
		assertEquals("관리자 권한 필요", e.getMessage());				
	}
	/**
	 * 로그인X -> 오류 발생
	 */
	//@Disabled
	@Test
	void UserIsNotLoggedInThrowException() {
		//1. 
		topicMapper.deleteAll();
		assertEquals(0,topicMapper.getCount());
		//2. 로그인 X -> 오류 발생	
		Exception e = assertThrows(NullPointerException.class,()->{
			service.doSave(topicDto);
		});
		assertEquals("로그인 필요", e.getMessage());
	}
	
	//@Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");

		assertNotNull(context);
		assertNotNull(topicMapper);
		assertNotNull(service);
		
		log.debug("context: {}"+context);
		log.debug("mapper: {}"+topicMapper);
		log.debug("service: {}"+service);
	}

}
