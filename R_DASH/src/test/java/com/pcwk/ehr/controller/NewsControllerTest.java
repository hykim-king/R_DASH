package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
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
import com.pcwk.ehr.domain.NewsDTO;
import com.pcwk.ehr.domain.TopicDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.NewsMapper;
import com.pcwk.ehr.mapper.TopicMapper;
import com.pcwk.ehr.service.NewsService;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
class NewsControllerTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	WebApplicationContext webApplicationContext;

	@Autowired
	NewsMapper newsMapper;
	
	@Autowired
	TopicMapper topicMapper;
	
	@Autowired
	NewsService service;

	// 웹 브라우저 대역 객체
	MockMvc mockMvc;

	TopicDTO tDto;
	
	NewsDTO nDto;

	SearchDTO search;
	
	UserDTO user;
	
	MockHttpSession session = new MockHttpSession();

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ setUp()               │");
		log.debug("└───────────────────────┘");
		// MockMvcBuilders.webAppContextSetup : (@WebAppConfiguration)를 기반으로 전체 웹 애플리케이션 환경을 구성
		//.build() : MockMvc 객체를 최종적으로 생성
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		tDto = new TopicDTO(0, "주제1", "주제내용1", "AI", "사용안함", null, "사용안함", 4.23);		search = new SearchDTO();
		nDto = new NewsDTO(0, "재난", "제목1", "naver.com123", "신문사1", "2025년8월15일", null);
		
		search = new SearchDTO();
		
		// 세션 생성 및 로그인 사용자 세팅
	    session = new MockHttpSession();
	    user = new UserDTO();
	    user.setEmail("ADMIN");
	    user.setRole(1);
	    session.setAttribute("user", user);
	
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ tearDown()            │");
		log.debug("└───────────────────────┘");
	}
	
	//1. 토픽 등록 Post00
	//2. 토픽 수정 Post00
	//3. 토픽 삭제 Post00
	//4. 토픽 다건 조회 Get00 (todayTopic)
	//5. 토픽 단건 조회 Get00
	//6. 뉴스 키워드 조회 Get00 searchByKeyword
	//7. 뉴스 전체 조회 Get00
	
	@Test
	void doRetrieve() throws Exception{
		log.debug("┌───────────────────────┐");
		log.debug("│ doRetrieve_news()     │");
		log.debug("└───────────────────────┘");
		
		newsMapper.deleteAll();
		assertEquals(0, newsMapper.getCount());
		
		int count = newsMapper.saveAll();
		assertEquals(100, count);
		
		MockHttpServletRequestBuilder requestBuilder 
		= MockMvcRequestBuilders.get("/news/doRetrieve.do")
				.param("pageNo", "1")
				.param("pageSize", "10");
	
		ResultActions resultActions = mockMvc.perform(requestBuilder)
								.andExpect(status().isOk());
		// 4.1 Model 데이터 조회
		MvcResult mvcResult = resultActions.andDo(print()).andReturn();
		// 4.2
		Map<String, Object> model = mvcResult.getModelAndView().getModel();
		List<NewsDTO> list = (List<NewsDTO>) model.get("list");
		
		for(NewsDTO vo : list) {
			log.debug(vo);
		}
		
		assertEquals(10, list.size());
	
	}
	
	@Disabled
	@Test
	void searchByKeyword() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ searchByKeyword()     │");
		log.debug("└───────────────────────┘");
		
		newsMapper.deleteAll();
		assertEquals(0, newsMapper.getCount());
		
		int count = newsMapper.saveAll();
		assertEquals(100, count);
		
		MockHttpServletRequestBuilder requestBuilder 
		= MockMvcRequestBuilders.get("/news/getNews.do")
				.param("keyword", nDto.getKeyword());
		
		ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(status().isOk());
		   MvcResult mvcResult = resultActions.andDo(print()).andReturn();
		   
		   Map<String, Object> model = mvcResult.getModelAndView().getModel();
		   List<NewsDTO> outList = (List<NewsDTO>) model.get("list");
		   log.debug("outList:{}", outList);
		   
		   String viewName = mvcResult.getModelAndView().getViewName();
		   log.debug("viewName:{}", viewName);
		   assertEquals("news/news_page", viewName);
		
	}
	
	@Disabled
	@Test
	void todayTopic() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ todayTopic()          │");
		log.debug("└───────────────────────┘");
		
		topicMapper.deleteAll();
		
		int flag = topicMapper.saveAll();
		assertEquals(100, flag);
		
		MockHttpServletRequestBuilder requestBuilder 
		= MockMvcRequestBuilders.get("/news/todayTopic.do")
				.param("topicNo", String.valueOf(tDto.getTopicNo()));


	   ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(status().isOk());
	   MvcResult mvcResult = resultActions.andDo(print()).andReturn();
	   
	   Map<String, Object> model = mvcResult.getModelAndView().getModel();
	   List<TopicDTO> outList = (List<TopicDTO>) model.get("list");
	   log.debug("outVO:{}", outList);
	   
	   String viewName = mvcResult.getModelAndView().getViewName();
	   log.debug("viewName:{}", viewName);
	   assertEquals("news/news_page", viewName);
		
		
	}
	
	@Disabled
	@Test
	void topicSelectOne() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ topicSelectOne()      │");
		log.debug("└───────────────────────┘");
		
		topicMapper.deleteAll();
		
		int flag = topicMapper.doSave(tDto);// 한건 등록
		assertEquals(1, flag);
		log.debug("after tDto:{}", tDto);
		
		MockHttpServletRequestBuilder requestBuilder 
		= MockMvcRequestBuilders.get("/news/topicSelectOne.do")
				.param("topicNo", String.valueOf(tDto.getTopicNo()));


	   ResultActions resultActions = mockMvc.perform(requestBuilder).andExpect(status().isOk());
	   MvcResult mvcResult = resultActions.andDo(print()).andReturn();
	   
	   Map<String, Object> model = mvcResult.getModelAndView().getModel();
	   TopicDTO outVO = (TopicDTO) model.get("vo");
	   log.debug("outVO:{}", outVO);
	   
	   String viewName = mvcResult.getModelAndView().getViewName();
	   log.debug("viewName:{}", viewName);
	   assertEquals("news/news_page", viewName);
		
	}
	
	@Disabled
	@Test
	void doDelete() throws Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ doDelete_topic()      │");
		log.debug("└───────────────────────┘");
		
		topicMapper.deleteAll();
		
		int flag = topicMapper.doSave(tDto);// 한건 등록
		assertEquals(1, flag);
		
		TopicDTO outVO = topicMapper.doSelectOne(tDto);
		assertNotNull(outVO);
		log.debug("outVO:{}", outVO);
		
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.post("/news/doDelete.do")
				.param("topicNo",String.valueOf(tDto.getTopicNo()));
		
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
		log.debug("│ doUpdate_topic()      │");
		log.debug("└───────────────────────┘");

		topicMapper.deleteAll();
		
		int flag = topicMapper.doSave(tDto);// 한건 등록
		assertEquals(1, flag);
		
		TopicDTO outVO = topicMapper.doSelectOne(tDto);
		assertNotNull(outVO);
		log.debug("outVO:{}", outVO);
		
		String upString = "U";
		int upInt = 1;
		
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.post("/news/doUpdate.do").session(session)
				.param("topicNo",String.valueOf(tDto.getTopicNo()))
				.param("title",tDto.getTitle()+upString)
				.param("contents",tDto.getContents()+upString)
				.param("reg_id",user.getEmail())
				.param("mod_id",user.getEmail());
		
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
		assertEquals("주제1U등록되었습니다.", resultMessage.getMessage());
	}
	
	@Disabled
	@Test
	void doSave() throws UnsupportedEncodingException, Exception {
		log.debug("┌───────────────────────┐");
		log.debug("│ doSave_topic()        │");
		log.debug("└───────────────────────┘");
		
		topicMapper.deleteAll();
		

		
		//1. url 호출 & param 전달
		MockHttpServletRequestBuilder requestBuilder = 
				MockMvcRequestBuilders.post("/news/doSave.do").session(session)
				.param("title",tDto.getTitle())
				.param("contents",tDto.getContents())
				.param("reg_id",user.getEmail())
				.param("mod_id",user.getEmail())
				.param("topic_ratio", String.valueOf(tDto.getTopicRatio()));
				
		
		//log.debug("String.valueOf(tDto.getTopicRatio()): "+String.valueOf(tDto.getTopicRatio()));
		
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
		assertEquals("주제1등록되었습니다.", resultMessage.getMessage());
	}
	
	@Disabled
	@Test
	void beans() {
		log.debug("┌───────────────────────┐");
		log.debug("│ beans()               │");
		log.debug("└───────────────────────┘");

		assertNotNull(webApplicationContext);
		assertNotNull(mockMvc);
		assertNotNull(newsMapper);
		assertNotNull(topicMapper);
		assertNotNull(service);
		
		log.debug("webApplicationContext:{}", webApplicationContext);
		log.debug("mockMvc:{}", mockMvc);
		log.debug("mapper:{}", newsMapper);
		log.debug("mapper:{}", topicMapper);
		log.debug("mapper:{}", service);
		
	}
	
}
