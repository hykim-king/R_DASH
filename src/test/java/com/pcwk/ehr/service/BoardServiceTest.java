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
import com.pcwk.ehr.domain.UserDTO;
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
	BoardServiceImpl service;
	
	BoardDTO dto;

	@Autowired
	MarkdownServiceImpl markdownService;

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
	@Test
	void convertToMarkdownHtml() {
		String markdown="###### SPRING";
		
		markdownService.convertToMarkdownHtml(markdown);
		log.debug(markdownService);
		
	}
	/**
	 * 일반 유저 조회 => 조회수 증가X
	 */
	@Disabled
	@Test
	void userGetView() {
		//1.
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		//2.
		mapper.doSave(dto);
		assertEquals(1, mapper.getCount());
		
		//3.조회
		BoardDTO outVo = mapper.doSelectOne(dto);
		int boardNo = outVo.getBoardNo();
		int getView = outVo.getViewCnt();
		log.debug("outVo: {}"+outVo);
		log.debug("boardNo: {}"+boardNo);
		log.debug("getView: {}"+getView);
		assertEquals(0, getView);
		
		//3. 사용자 객체 생성
		UserDTO user = new UserDTO();
		
		user.setNickname("user01");
		user.setRole(2);
		
		//로그인 여부 상관없이 조회수 오름.
		service.session.setAttribute("user", user);
		
		//4. 조회수 확인(관리자일 때는 조회수 오르지 X)
		BoardDTO inVo = service.doSelectOne(outVo);
		log.debug("inVo: {}"+inVo);
		
		int afterView = inVo.getViewCnt();
		log.debug("afterView: {}"+afterView);
		assertEquals(1, afterView);
	}
	
	/**
	 * 관리자 조회 => 조회수 증가 X
	 */
	@Disabled
	@Test
	void adminGetView() {
		//1.
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		//2.
		mapper.doSave(dto);
		assertEquals(1, mapper.getCount());
		
		//3.조회
		BoardDTO outVo = mapper.doSelectOne(dto);
		int boardNo = outVo.getBoardNo();
		int getView = outVo.getViewCnt();
		log.debug("outVo: {}"+outVo);
		log.debug("boardNo: {}"+boardNo);
		log.debug("getView: {}"+getView);
		assertEquals(0, getView);
		
		//3. 사용자 객체 생성
		UserDTO user = new UserDTO();
		
		user.setNickname("ADMIN");
		user.setRole(1);
		
		service.session.setAttribute("user", user);
		
		//4. 조회수 확인(관리자일 때는 조회수 오르지 X)
		BoardDTO inVo = service.doSelectOne(outVo);
		log.debug("inVo: {}"+inVo);
		
		int afterView = inVo.getViewCnt();
		log.debug("afterView: {}"+afterView);
		assertEquals(0, afterView);
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
		assertNotNull(markdownService);
		
		log.debug("context: {}"+context);
		log.debug("mapper: {}"+mapper);
		log.debug("service: {}"+service);
		log.debug("markdownService: {}"+markdownService);
	}

}
