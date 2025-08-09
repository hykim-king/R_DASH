package com.pcwk.ehr.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.service.BotService;
import com.pcwk.ehr.service.ChatService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextHierarchy({
		// 1) 프로덕션 XML 컨텍스트
		@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
				"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml" }),
		// 2) 테스트 전용 Mock 빈 컨텍스트 (실제 빈보다 우선 주입)
		@ContextConfiguration(classes = ChatControllerTest.TestConfig.class) })
@DirtiesContext // (선택) 테스트 간 컨텍스트 캐시 영향 최소화
class ChatControllerTest {

	private static final Logger log = LogManager.getLogger(ChatControllerTest.class);

	@Configuration
	static class TestConfig {
		@Bean
		@Primary
		ChatService chatService() {
			return Mockito.mock(ChatService.class);
		}

		@Bean
		@Primary
		BotService botService() {
			return Mockito.mock(BotService.class);
		}
	}

	// 필드 주입
	// 스프링이 위에서 등록한 목 빈을 이 필드에 주입한다.
	@Resource
	WebApplicationContext wac;
	@Resource
	ChatService chatService; // mock 주입
	@Resource
	BotService botService; // mock 주입

	// 공통 셋업
	// ObjectMapper는 DTO ↔ JSON 직렬화/역직렬화에 사용.
	MockMvc mockMvc;
	ObjectMapper om = new ObjectMapper();

	@BeforeEach
	void setUp() {
		Assertions.assertNotNull(wac, "WebApplicationContext 주입 실패");
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	@DisplayName("POST /api/chat/send - BotService 응답 사용 + 저장 성공")
	void send_withBot_ok() throws Exception {
		ChatDTO req = new ChatDTO(null, 100, "sess-1", "안녕?", null, null);

		when(botService.reply(any(String.class), any(String.class), any(Integer.class))).thenReturn("안녕하세요!");
		when(chatService.insertChat(any(ChatDTO.class))).thenReturn(1);

		mockMvc.perform(post("/api/chat/send").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON)
				.header("X-Session-Id", "sess-1").content(om.writeValueAsString(req))).andExpect(status().isOk())
				.andExpect(jsonPath("$.question").value("안녕?")).andExpect(jsonPath("$.answer").value("안녕하세요!"));
	}

	@Test
	@DisplayName("POST /api/chat/send - 빈 질문이면 400")
	void send_badRequest_empty() throws Exception {
		ChatDTO req = new ChatDTO(null, 100, "sess-1", "   ", null, null);

		mockMvc.perform(post("/api/chat/send").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(req))).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("GET /api/chat/{logNo} - 존재하면 200, 없으면 404")
	void get_one_found_and_notFound() throws Exception {
		ChatDTO found = new ChatDTO(10L, 100, "sess-1", "Q", "A", "2025-08-08 12:00:00");
		Mockito.when(chatService.selectChat(10L)).thenReturn(found);
		Mockito.when(chatService.selectChat(999L)).thenReturn(null);

		mockMvc.perform(get("/api/chat/10").characterEncoding("UTF-8")).andExpect(status().isOk())
				.andExpect(jsonPath("$.logNo").value(10));

		mockMvc.perform(get("/api/chat/999").characterEncoding("UTF-8")).andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("POST /api/chat/list - 목록 반환 200")
	void list_ok() throws Exception {
		SearchDTO search = new SearchDTO(1, 1, "10", "안녕");
		ChatDTO row = new ChatDTO(1L, 100, "sess-1", "안녕", "안녕하세요!", "2025-08-08 12:00:00");

		Mockito.when(chatService.chatList(any(SearchDTO.class))).thenReturn(Arrays.asList(row));

		mockMvc.perform(post("/api/chat/list").characterEncoding("UTF-8").contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(search))).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].question").value("안녕"));
	}

	@Test
	@DisplayName("DELETE /api/chat/{logNo} - 성공 200, 실패 404")
	void delete_ok_and_notFound() throws Exception {
		Mockito.when(chatService.deleteChat(1L)).thenReturn(1);
		Mockito.when(chatService.deleteChat(2L)).thenReturn(0);

		mockMvc.perform(delete("/api/chat/1").characterEncoding("UTF-8")).andExpect(status().isOk())
				.andExpect(content().string("삭제 성공"));

		mockMvc.perform(delete("/api/chat/2").characterEncoding("UTF-8")).andExpect(status().isNotFound())
				.andExpect(content().string("삭제 실패"));
	}
}