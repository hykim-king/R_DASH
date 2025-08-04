package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.mapper.BoardMapper;

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
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
		
		dto01 = new BoardDTO(0, "제목1","10", "내용1", 0, "사용안함", "ADMIN", "사용안함", "ADMIN");
		search = new SearchDTO();
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}

	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");
		
		assertNotNull(context);
		assertNotNull(mapper);
		assertNotNull(dto01);
		assertNotNull(search);
		
		log.debug("context: {}"+context);
		log.debug("mapper: {}"+mapper);
		log.debug("dto01: {}"+dto01);
		log.debug("search: {}"+search);
	}

}
