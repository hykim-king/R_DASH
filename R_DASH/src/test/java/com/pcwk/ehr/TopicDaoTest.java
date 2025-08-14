package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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
import com.pcwk.ehr.mapper.TopicMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
class TopicDaoTest {

	Logger log = LogManager.getLogger(getClass());
	
	TopicDTO dto01;

	@Autowired
	TopicMapper mapper;

	@Autowired
	ApplicationContext context;

	// 1. 저장 0
	// 2. 수정 0
	// 3. 삭제 0
	// 4. 단 건 조회 0
	// 5. 다 건 조회

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");

		dto01 = new TopicDTO(0, "주제1", "주제의 내용1", "AI", "사용안함", "ADMIN", "사용안함", 4.23);
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}
	@Test
	void getTodayTopicsList() {
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		// 다건 등록
		mapper.saveAll();
		assertEquals(100, mapper.getCount());
		
		// 오늘 토픽 조회
		List<TopicDTO> outVO = mapper.getTodayTopicsList(dto01);
		log.debug("outVO: {}"+outVO);
		log.debug("outVO.size(): {}"+outVO.size());
		assertEquals(25, outVO.size());
	}
	
	//@Disabled
	@Test
	void UpdateAndSelectOne() {
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());

		int flag = mapper.doSave(dto01);
		assertEquals(1, flag);
		
		// 단 건 조회
		TopicDTO outVO = mapper.doSelectOne(dto01);
		log.debug("outVO: {}"+outVO);
		
		// 단 건 업데이트 
		outVO.setTitle(outVO.getTitle()+"_U");
		outVO.setContents(outVO.getContents()+"_U");
		outVO.setModId("ADMIN");
		outVO.setTopicRatio(null);
		
		flag = mapper.doUpdate(outVO);
		assertEquals(1, flag);
		
		TopicDTO inVO = mapper.doSelectOne(outVO);
		log.debug("inVO: {}"+inVO);
		
		// 비교
		assertEquals(outVO.getTitle(), inVO.getTitle());
		assertEquals(outVO.getContents(), inVO.getContents());
		assertEquals(outVO.getModId(), inVO.getModId());
		assertEquals(outVO.getTopicRatio(), inVO.getTopicRatio());
		
	}
	/**
	 * 단 건 등록 -> 단 건 조회 -> 단 건 삭제 테스트
	 */
	//@Disabled
	@Test
	void SaveAndDelete() {
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());

		int flag = mapper.doSave(dto01);
		assertEquals(1, flag);
		
		mapper.doDelete(dto01); 
		assertEquals(1, flag);
		assertEquals(0, mapper.getCount());

	}

	//@Disabled
	@Test
	void beans() {
		assertNotNull(mapper);
		assertNotNull(context);

		log.debug("mapper: {}" + mapper);
		log.debug("context: {}" + context);
	}

}
