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

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.domain.NewsDTO;
import com.pcwk.ehr.mapper.NewsMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"		
									,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"})
class NewsDaoTest {
	
	Logger log = LogManager.getLogger(getClass());
	
	NewsDTO dto;
	NewsDTO dto1;
	NewsDTO dto2;
	
	@Autowired
	NewsMapper mapper;
	
	@Autowired
	ApplicationContext context;
	
	SearchDTO search;
	
	// 1. 저장 0
	// 2. merge(2.1 있으면 -> 업데이트, 2.2 없으면 -> 저장)0
	// 3. 조회 (키워드 넣으면 조회 잘 되는지)

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
		
		dto = new NewsDTO(0, "재난", "제목1", "naver.com123", "신문사1", "2025년8월15일", null);
		dto1 = new NewsDTO(0, "폭우", "제목_업데이트", "naver.com123", "신문사2", "2025년8월15일", null);
		dto2 = new NewsDTO(0, "홍수", "제목_삽입", "naver.com456", "신문사3", "2025년8월15일", null);
		
		search = new SearchDTO();
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}
	@Test
	void doRetrieve() {
		//1. 전체 삭제
		//2. 다 건 등록
		//3. 다 건 조회
		//4. 건수 확인
		
		//1. 전체를 삭제
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
				
		//2. 다 건 등록
		int count = mapper.saveAll();
		log.debug("count: {}"+count);
		assertEquals(100, count);
		
		search.setPageSize(10);
		search.setPageNo(1);
		
		//3. 다건 조회
		List<NewsDTO> outVO = mapper.doRetrieve(search);
		log.debug("outVO: {}"+outVO);
		
		for(NewsDTO dto : outVO) {
			log.debug("dto :{}",dto);
		}
		
	}
	//@Disabled
	@Test
	void searchByKeyword() {
		//1. 전체 삭제
		//2. 다 건 등록
		//3. 키워드별 조회
		
		//1. 전체를 삭제
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		//2. 다 건 등록
		int count = mapper.saveAll();
		log.debug("count: {}"+count);
		assertEquals(100, count);
		
		//3. 키워드로 조회
		List<NewsDTO> outVO = mapper.searchByKeyword(dto);
		log.debug("outVO: {}"+outVO);
		log.debug("outVO.size(): {}"+outVO.size());
		assertEquals(33, outVO.size());
	}
	
	//@Disabled
	@Test
	void merge() {
		//1. 전체를 삭제
		//2. 한 건 등록
		//3. 업데이트 시도(url 같을 경우) dto1
		//1. 전체를 삭제
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		//2. 단건 저장
		int flag = mapper.doSave(dto);
		log.debug("flag: {}"+flag);
		assertEquals(1, flag);	
		
		NewsDTO outVO = mapper.doSelectOne(dto);
		log.debug("outVO: {}"+outVO);
		
		//3. 같으면 업데이트
		flag = mapper.doUpdate(dto1);
		assertEquals(1, flag);
		NewsDTO updateVO = mapper.doSelectOne(dto1);
		log.debug("updateVO: {}"+updateVO);
		//4. 다르면 저장
		flag = mapper.doUpdate(dto2);
		assertEquals(1, flag);
		NewsDTO newInsertVO = mapper.doSelectOne(dto2);
		log.debug("newInsertVO: {}"+newInsertVO);
		
		assertEquals(2, mapper.getCount());
		log.debug("mapper.getCount(): {}"+mapper.getCount());

	}
	
	//@Disabled
	@Test
	void doSave() {
		//1. 전체를 삭제
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());
		
		//2. 단건 저장
		int flag = mapper.doSave(dto);
		log.debug("flag: {}"+flag);
		assertEquals(1, flag);
		
		//3. 조회
		NewsDTO outVO = mapper.doSelectOne(dto);
		log.debug("outVO: {}"+outVO);

	}
	
	//@Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");
		
		assertNotNull(mapper);
		assertNotNull(context);
		
		log.debug("mapper: {}"+mapper);
		log.debug("context: {}"+context);
	}

}
