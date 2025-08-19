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
import com.pcwk.ehr.mapper.BoardMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"		
									,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"})
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
		
		dto01 = new BoardDTO(0, "제목1", "내용1",0, "사용안함", "ADMIN", "사용안함", "ADMIN",null);
		search = new SearchDTO();
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}
	@Test
	void updateViews() {
		//1.전체 삭제
		mapper.deleteAll();
		//2. 한 건 등록
		int flag = mapper.doSave(dto01);
		assertEquals(1, flag);
		assertEquals(1,mapper.getCount());
		//3. 뷰 업데이트
		flag = mapper.updateViews(dto01);
		assertEquals(1, flag);
		
		//4. 확인
		BoardDTO outVO = mapper.doSelectOne(dto01);
		int view = outVO.getViewCnt();
		log.debug("view: {}"+view);
		assertEquals(1, view);

	}
	//@Disabled
	@Test
	void doRetrieve() {
		log.debug("┌────────────────────┐");
		log.debug("│ doUpdate()         │");
		log.debug("└────────────────────┘");
		//1.전체 삭제
		mapper.deleteAll();
		//2.다건 등록
		int flag = mapper.saveAll();
		log.debug("flag:{}",flag);
		assertEquals(502, flag);
		
		//3.다건 조회
		search.setPageNo(1);
		search.setPageSize(10);
		search.setSearchDiv("40");
		search.setSearchWord("제목1");
		
		List<BoardDTO> list = mapper.doRetrieve(search);
		for(BoardDTO dto : list) {
			log.debug("dto :{}",dto);
		}
	
	}
	
	//@Disabled
	@Test
	void doUpdate() {
		log.debug("┌────────────────────┐");
		log.debug("│ doUpdate()         │");
		log.debug("└────────────────────┘");
		//1.전체 삭제
		mapper.deleteAll();
		//2. 한 건 등록
		int flag = mapper.doSave(dto01);
		assertEquals(1, flag);
		assertEquals(1,mapper.getCount());
		
		//3.한 건 조회
		BoardDTO outVO = mapper.doSelectOne(dto01);
		log.debug("outVO: {}"+outVO);
		
		isSameBoard(dto01, outVO);
		//4. 업데이트
		outVO.setTitle(outVO.getTitle()+"_U");
		outVO.setContents(outVO.getContents()+"_U");
		
		flag = mapper.doUpdate(outVO);
		assertEquals(1, flag);

		//5.비교
		BoardDTO inVO = mapper.doSelectOne(outVO);
		isSameBoard(inVO, outVO);
	}
	
	//@Disabled
	@Test
	void saveAndDelete() {
		log.debug("┌────────────────────┐");
		log.debug("│ doSave()           │");
		log.debug("└────────────────────┘");
		//1.전체 삭제
		mapper.deleteAll();
		//2. 한 건 등록
		int flag = mapper.doSave(dto01);
		assertEquals(1, flag);
		assertEquals(1,mapper.getCount());
		//3.한 건 조회
		BoardDTO outVO = mapper.doSelectOne(dto01);
		log.debug("outVO: {}"+outVO);
		
		flag = mapper.doDelete(dto01);
		assertEquals(1, flag);
		assertEquals(0,mapper.getCount());
	}
	
	//@Disabled
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
	void isSameBoard(BoardDTO outVO,BoardDTO dto01) {
		assertEquals(outVO.getBoardNo(),dto01.getBoardNo());
		assertEquals(outVO.getTitle(),dto01.getTitle());
		assertEquals(outVO.getContents(),dto01.getContents());
		assertEquals(outVO.getViewCnt(),dto01.getViewCnt());
		assertEquals(outVO.getModId(),dto01.getModId());

	}

}
