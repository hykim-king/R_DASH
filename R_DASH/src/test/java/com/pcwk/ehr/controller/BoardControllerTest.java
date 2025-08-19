package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.MessageDTO;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.mapper.BoardMapper;
import com.pcwk.ehr.service.BoardService;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })

class BoardControllerTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	BoardMapper mapper;
	
	@Autowired
	BoardService service;
	

	// 웹 브라우저 대역 객체
	MockMvc mockMvc;

	BoardDTO dto;

	SearchDTO search;

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ setUp()               │");
		log.debug("└───────────────────────┘");
		// MockMvcBuilders.webAppContextSetup : (@WebAppConfiguration)를 기반으로 전체 웹 애플리케이션 환경을 구성
		//.build() : MockMvc 객체를 최종적으로 생성
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		dto = new BoardDTO(0, "제목1", "내용1",0, "사용안함", "ADMIN", "사용안함", "ADMIN",null);
		search = new SearchDTO();
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ tearDown()            │");
		log.debug("└───────────────────────┘");
	}
	//doSelectOne
	//doRetieve
	//@Disabled
	@Test
	void doSelectOne() throws Exception{
		log.debug("┌────────────────────────────┐");
		log.debug("│ doSelectOne()              │");
		log.debug("└────────────────────────────┘");
		// 1.전체삭제
		// 2.등록
		// 3.단건조회
		
		// 1.
		mapper.deleteAll();

		// 2.
		log.debug("before:{}", dto);
		int flag = mapper.doSave(dto);// 한건 등록
		assertEquals(1, flag);
		log.debug("after:{}", dto);
		
		// 3
		MockHttpServletRequestBuilder requestBuilder 
			= MockMvcRequestBuilders.get("/board/doSelectOne.do")
					.param("boardNo", String.valueOf(dto.getBoardNo()));
	

		ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(status().isOk());
		MvcResult mvcResult = resultActions.andDo(print()).andReturn();
		
		Map<String, Object> model = mvcResult.getModelAndView().getModel();
		BoardDTO outVO = (BoardDTO) model.get("vo");
		log.debug("outVO:{}", outVO);
		
		String viewName = mvcResult.getModelAndView().getViewName();
		log.debug("viewName:{}", viewName);
		assertEquals("board/board_view", viewName);

	}
	
	//@Disabled
	@Test
	void doRetrieve() throws Exception{
		log.debug("┌────────────────────────────┐");
		log.debug("│ doRetrieve()               │");
		log.debug("└────────────────────────────┘");

		// 1.전체 삭제
		// 2.다건 등록(saveAll)
		// 3.목록 조회
		// 4.비교
		
		// 1.
		mapper.deleteAll();
		assertEquals(0, mapper.getCount());

		// 2.
		mapper.saveAll();
		assertEquals(502, mapper.getCount());
		
		// 3.
		MockHttpServletRequestBuilder requestBuilder 
			= MockMvcRequestBuilders.get("/board/doRetrieve.do")
					.param("pageNo", "1")
					.param("pageSize", "10")
					.param("searchDiv", "10")
					.param("searchWord", "1");
		
		ResultActions resultActions = mockMvc.perform(requestBuilder)
								.andExpect(status().isOk());

		// 4.1 Model 데이터 조회
		MvcResult mvcResult = resultActions.andDo(print()).andReturn();
		// 4.2
		Map<String, Object> model = mvcResult.getModelAndView().getModel();
		
		List<BoardDTO> list = (List<BoardDTO>) model.get("list");
		for(BoardDTO vo : list) {
			log.debug(vo);
		}
		
		assertEquals(10, list.size());
	}
	
	@Disabled
	@Test
	void doDelete() throws Exception {
		log.debug("┌────────────────────────────┐");
		log.debug("│ doDelete()                 │");
		log.debug("└────────────────────────────┘");
		// 1.전체삭제
		// 2.등록
		// 3.삭제

		// 1.
		mapper.deleteAll();
		// 2. 
		int flag = mapper.doSave(dto);// 한건 등록
		assertEquals(1, flag);
		// 3.
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/board/doDelete.do")
				.param("boardNo",String.valueOf(dto.getBoardNo()));

		ResultActions resultActions = mockMvc.perform(requestBuilder)
								.andExpect(status().isOk())
								.andExpect(MockMvcResultMatchers.content()
										.contentType("text/plain;charset=UTF-8"));

		// 2.3
		String returnBody = resultActions.andDo(print()).andReturn().getResponse().getContentAsString();

		log.debug("returnBody:{}", returnBody);
		MessageDTO resultMessage = new Gson().fromJson(returnBody, MessageDTO.class);
		log.debug("resultMessage: {}", resultMessage);

		assertEquals(1, resultMessage.getMessageId());
		assertEquals("삭제 되었습니다.", resultMessage.getMessage());
	}
	
	@Disabled
	@Test
	void doUpdate() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ doUpdate()            │");
		log.debug("└───────────────────────┘");

		mapper.deleteAll();
		
		int flag = mapper.doSave(dto);// 한건 등록
		assertEquals(1, flag);
		
		BoardDTO outVO = mapper.doSelectOne(dto);
		assertNotNull(outVO);
		log.debug("outVO:{}", outVO);
		
		String upString = "U";
		int upInt = 99;
		
		// 2.1 url호출, method:post, param
		MockHttpServletRequestBuilder requestBuilder = 
					MockMvcRequestBuilders.post("/board/doUpdate.do")
					.param("boardNo",String.valueOf(outVO.getBoardNo()))
					.param("title",outVO.getTitle()+upString)
					.param("contents", dto.getContents()+upString)
					.param("modId", dto.getModId()+upString);
		log.debug("requestBuilder:{}", requestBuilder);
		//2.2 호출
		ResultActions resultActions = mockMvc.perform(requestBuilder)
									.andExpect(status().isOk())
									.andExpect(MockMvcResultMatchers.content()
											.contentType("text/plain;charset=UTF-8"));
		//2.3  호출 결과 받기
		String returnBody = resultActions.andDo(print())
										.andReturn()
										.getResponse()
										.getContentAsString(); 
		log.debug("returnBody:{}", returnBody);
		
		MessageDTO resultMessage = new Gson().fromJson(returnBody, MessageDTO.class);
		log.debug("resultMessage:{}", resultMessage);
		assertEquals("수정 되었습니다.", resultMessage.getMessage());
		

	}
	
	//@Disabled
	@Test
	void doSave() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ doSave()              │");
		log.debug("└───────────────────────┘");

		mapper.deleteAll();
		
		//1. url 호출 & param 전달
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.post("/board/doSave.do")
				.param("title", dto.getTitle())
				.param("contents", dto.getContents())
				.param("regId", dto.getRegId())
				.param("modId", dto.getRegId());
		//2. 호출
		ResultActions resultActions = mockMvc
										.perform(requestBuilder)
										.andExpect(status().isOk())
										.andExpect(MockMvcResultMatchers.content()
												.contentType("text/plain;charset=UTF-8"));
		//3. 호출 결과 받기
		String returnBody = resultActions.andDo(print())
										.andReturn()
										.getResponse()
										.getContentAsString();
		log.debug("returnBody:{}", returnBody);
		
		//4. json to MessageDTO 변환
		MessageDTO resultMessage = new Gson().fromJson(returnBody, MessageDTO.class);

		log.debug("resultMessage:{}", resultMessage.toString());
		assertEquals(1, resultMessage.getMessageId());
		assertEquals("제목1등록되었습니다.", resultMessage.getMessage());
	}
	
	//@Disabled
	@Test
	void beans() {
		log.debug("┌───────────────────────┐");
		log.debug("│ beans()               │");
		log.debug("└───────────────────────┘");

		assertNotNull(webApplicationContext);
		assertNotNull(mockMvc);
		assertNotNull(mapper);

		log.debug("webApplicationContext:{}", webApplicationContext);
		log.debug("mockMvc:{}", mockMvc);
		log.debug("mapper:{}", mapper);
	}
	void isSameBoard(BoardDTO outVO,BoardDTO dto01) {
		assertEquals(outVO.getBoardNo(),dto01.getBoardNo());
		assertEquals(outVO.getTitle(),dto01.getTitle());
		assertEquals(outVO.getContents(),dto01.getContents());
		assertEquals(outVO.getViewCnt(),dto01.getViewCnt());
		assertEquals(outVO.getModId(),dto01.getModId());

	}

}
