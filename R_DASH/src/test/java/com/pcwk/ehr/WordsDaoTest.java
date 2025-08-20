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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.domain.TopicWordsDTO;
import com.pcwk.ehr.mapper.BoardMapper;
import com.pcwk.ehr.mapper.TopicWordsMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"		
									,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"})
class WordsDaoTest {
		
	@Autowired
	@Qualifier("topicWordsMapper")
	TopicWordsMapper mapper;
	
	@Autowired
	ApplicationContext context;
	
	Logger log = LogManager.getLogger(getClass());

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
		
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}
	@Test
	void getChangeRate() {
		log.debug("┌────────────────────┐");
		log.debug("│ getChangeRate()    │");
		log.debug("└────────────────────┘");
		
		//1.전체 삭제
		//2.다 건 등록
		//3.top10조회
		//4.개수 확인
		
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		mapper.saveAll();
		assertEquals(100, mapper.getCount());
		
		List<TopicWordsDTO> outVO = mapper.getChangeRate();
		log.debug("outVO: {}"+outVO);
		assertEquals(5, outVO.size());
		for(TopicWordsDTO vo : outVO) {
			log.debug("vo: {}"+vo);
		}
		
	}
	
	@Disabled
	@Test
	void top10() {
		log.debug("┌────────────────────┐");
		log.debug("│ top10()            │");
		log.debug("└────────────────────┘");
		
		//1.전체 삭제
		//2.다 건 등록
		//3.top10조회
		//4.개수 확인
		
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		mapper.saveAll();
		assertEquals(100, mapper.getCount());
		
		List<TopicWordsDTO> outVO = mapper.top10();
		log.debug("outVO: {}"+outVO);
		assertEquals(10, outVO.size());
		for(TopicWordsDTO vo : outVO) {
			log.debug("vo: {}"+vo);
		}
		
	}
	@Disabled
	@Test
	void top100() {
		log.debug("┌────────────────────┐");
		log.debug("│ top100()           │");
		log.debug("└────────────────────┘");
		
		//1.전체 삭제
		//2.다 건 등록
		//3.top100조회
		//4.개수 확인
		
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		mapper.saveAll();
		assertEquals(100, mapper.getCount());
		
		List<TopicWordsDTO> outVO = mapper.top100();
		log.debug("outVO: {}"+outVO);
		assertEquals(100, mapper.getCount());
		
	}
	
	//@Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");
		
		assertNotNull(context);
		assertNotNull(mapper);

		log.debug("context: {}"+context);
		log.debug("mapper: {}"+mapper);

	}

}
