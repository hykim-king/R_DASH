package com.pcwk.ehr.controller;

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

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.mapper.BoardMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"		
									,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
class BoardDaoTest {
	
	BoardDTO dto01;
	SearchDTO search;
	
	@Autowired
	BoardMapper mapper;
	
	@Autowired
	ApplicationContext context;
	
	Logger log = LogManager.getLogger(getClass());

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌──────────────────┐");
		log.debug("│ setUp()          │");
		log.debug("└──────────────────┘");
		
		dto01 = new BoardDTO(0,"제목1","내용1", "88888888888.jsp",0,"사용안함", "ADMIN", "사용안함", "ADMIN");
		search = new SearchDTO();
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌──────────────────┐");
		log.debug("│ tearDown()       │");
		log.debug("└──────────────────┘");
	}
	@Test
	void doSave() {
		//1.전체 삭제
		mapper.deleteAll();
		//2. 한 건 등록
		int flag = mapper.doSave(dto01);
		assertEquals(1, flag);
		assertEquals(1,mapper.getCount());
		
		
	}
	
	@Disabled
	@Test
	void beans() {
		assertNotNull(context);
		assertNotNull(mapper);
		assertNotNull(dto01);
		assertNotNull(search);
		
		log.debug("context: {}"+context);
		log.debug("mapper: {}"+mapper);
		log.debug("dto01: {}"+dto01);
		log.debug("search: {}"+search);
	}
	
	void isSameBoard(BoardDTO outVO,BoardDTO dto01) {
		assertEquals(outVO.getBoardNo(),dto01.getBoardNo());
		assertEquals(outVO.getTitle(),dto01.getTitle());
		assertEquals(outVO.getContents(),dto01.getContents());
		assertEquals(outVO.getImage(),dto01.getImage());
		assertEquals(outVO.getViewCnt(),dto01.getViewCnt());
		assertEquals(outVO.getRegDt(),dto01.getRegDt());
		assertEquals(outVO.getRegId(),dto01.getRegId());
		assertEquals(outVO.getModDt(),dto01.getModDt());
		assertEquals(outVO.getModId(),dto01.getModId());

	}

}
