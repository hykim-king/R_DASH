package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.service.ChatService;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
class ChatControllerTest {

	private static final Logger log = LogManager.getLogger(ChatControllerTest.class);
	private static final MediaType APP_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

	@Autowired
	WebApplicationContext wac;
	@Autowired
	ChatService chatService;

	MockMvc mockMvc;
	Gson gson;

	@BeforeEach
	void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		this.gson = new Gson();
	}

	@AfterEach
	void tearDown() {
		log.debug("테스트 종료");
	}

	//@Disabled
	@Test
	void sendChat_success() throws Exception {
		ChatDTO in = new ChatDTO();
		in.setQuestion("안녕, 너는 누구야?");

		String body = gson.toJson(in);

		MvcResult mr = mockMvc
				.perform(MockMvcRequestBuilders.post("/api/chat/send").contentType(APP_JSON_UTF8)
						.header("X-Session-Id", "test-session-001").content(body))
				.andExpect(status().isOk()).andExpect(header().string("X-Session-Id", "test-session-001"))
				.andExpect(content().contentType("application/json;charset=UTF-8")).andReturn();

		ChatDTO out = gson.fromJson(mr.getResponse().getContentAsString(), ChatDTO.class);
		assertNotNull(out);
		assertEquals("test-session-001", out.getSessionId());
		assertEquals("안녕, 너는 누구야?", out.getQuestion());
		assertNotNull(out.getAnswer()); // FakeBotService → "테스트 응답"
	}

	//@Disabled
	@Test
	void sendChat_badRequest_empty() throws Exception {
		ChatDTO in = new ChatDTO();
		in.setQuestion("   "); // 공백만

		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/chat/send").contentType(APP_JSON_UTF8).content(gson.toJson(in)))
				.andExpect(status().isBadRequest());
	}

	//@Disabled
	@Test
	void getChat_byLogNo() throws Exception {
		// 1) 하나 생성
		ChatDTO in = new ChatDTO();
		in.setQuestion("재난 행동 요령 알려줘");
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/chat/send").contentType(APP_JSON_UTF8).content(gson.toJson(in)))
				.andExpect(status().isOk());

		// 2) 최근 1건 조회해서 logNo 확보
		SearchDTO s = new SearchDTO();
		s.setPageNo(1);
		s.setPageSize(1);
		List<ChatDTO> recent = chatService.chatList(s);
		assertFalse(recent.isEmpty());
		Long logNo = recent.get(0).getLogNo();
		assertNotNull(logNo);

		// 3) 단건 API
		MvcResult mr = mockMvc.perform(MockMvcRequestBuilders.get("/api/chat/{logNo}", logNo))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();

		ChatDTO out = gson.fromJson(mr.getResponse().getContentAsString(), ChatDTO.class);
		assertEquals(logNo, out.getLogNo());
	}

	//@Disabled
	@Test
	void chatList_withSearch() throws Exception {
		// 3건 생성
		for (int i = 0; i < 3; i++) {
			ChatDTO in = new ChatDTO();
			in.setQuestion("질문" + i);
			mockMvc.perform(
					MockMvcRequestBuilders.post("/api/chat/send").contentType(APP_JSON_UTF8).content(gson.toJson(in)))
					.andExpect(status().isOk());
		}

		SearchDTO search = new SearchDTO();
		search.setPageNo(1);
		search.setPageSize(10);
		search.setSearchDiv("10");
		search.setSearchWord("질문");

		MvcResult mr = mockMvc
				.perform(MockMvcRequestBuilders.post("/api/chat/list").contentType(APP_JSON_UTF8)
						.content(gson.toJson(search)))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
				.andReturn();

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = gson.fromJson(mr.getResponse().getContentAsString(), List.class);

		assertNotNull(list);
		assertTrue(list.size() >= 3);
	}

	//@Disabled
	@Test
	void deleteChat_ok() throws Exception {
		// 생성
		ChatDTO in = new ChatDTO();
		in.setQuestion("삭제 테스트");
		mockMvc.perform(
				MockMvcRequestBuilders.post("/api/chat/send").contentType(APP_JSON_UTF8).content(gson.toJson(in)))
				.andExpect(status().isOk());

		// 최근 1건 logNo
		SearchDTO s = new SearchDTO();
		s.setPageNo(1);
		s.setPageSize(1);
		Long logNo = chatService.chatList(s).get(0).getLogNo();

		mockMvc.perform(MockMvcRequestBuilders.delete("/api/chat/{logNo}", logNo)).andExpect(status().isOk());
	}

	//@Disabled
	@Test
  	void beans() {
  		log.debug("┌────────────────────┐");
  		log.debug("│ beans()            │");
  		log.debug("└────────────────────┘");
  		
  		assertNotNull(wac);
  		assertNotNull(mockMvc);
  		assertNotNull(mockMvc);
  		
  		
  		log.debug("wac: {}"+wac);
  		log.debug("mockMvc: {}"+mockMvc);
  		log.debug("chatService: {}"+chatService);
  		
  	}

}