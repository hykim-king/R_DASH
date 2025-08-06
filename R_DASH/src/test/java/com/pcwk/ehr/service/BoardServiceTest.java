package com.pcwk.ehr.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.mapper.BoardMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"		
									,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"})
class BoardServiceTest {
	
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	ApplicationContext context;
	
	@Autowired
	BoardMapper mapper;
	
	@Autowired
	BoardService service;
	
	BoardDTO dto;
	

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
		
		dto = new BoardDTO(0, "제목1", "내용1", "이미지1.png",0, "사용안함", "ADMIN", "사용안함", "ADMIN");

	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
		
		
	}
	/**
	 * 관리자가 아닌 사용자일 때만 조회수 증가
	 */
	@Test
	void doSelectOneGetView() {
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
	}
	
	@Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");

		assertNotNull(context);
		assertNotNull(mapper);
		assertNotNull(service);
		
		log.debug("context: {}"+context);
		log.debug("mapper: {}"+mapper);
		log.debug("service: {}"+service);
	}

}
