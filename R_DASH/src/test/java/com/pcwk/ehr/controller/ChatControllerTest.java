package com.pcwk.ehr.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.service.BotService;
import com.pcwk.ehr.service.ChatService;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class ChatControllerTest {

	Logger log = LogManager.getLogger(ChatControllerTest.class);

	@Autowired
	ChatService chatService;
	@Autowired
	BotService botService;

	MockMvc mockMvc;
	private ObjectMapper om = new ObjectMapper();

	private static final MediaType APP_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);

	@Before
	public void setUp() {
		// 컨트롤러를 직접 생성해서 mock 주입
		ChatController controller = new ChatController(chatService, botService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		Mockito.reset(chatService, botService);
	}

	@After
	public void tearDown() {
		log.debug("tearDown()");
	}

	@Disabled
	@Test
	public void send_withBot_ok() throws Exception {
		ChatDTO req = new ChatDTO(null, 100, "sess-1", "안녕?", null, null);

		when(botService.reply(any(String.class), any(String.class), any(Integer.class))).thenReturn("안녕하세요!");
		when(chatService.insertChat(any(ChatDTO.class))).thenReturn(1);

		mockMvc.perform(post("/api/chat/send").characterEncoding("UTF-8").contentType(APP_JSON_UTF8)
				.header("X-Session-Id", "sess-1").content(om.writeValueAsString(req))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.question").value("안녕?")).andExpect(jsonPath("$.answer").value("안녕하세요!"));

		verify(botService, times(1)).reply(eq("안녕?"), eq("sess-1"), eq(100));

		ArgumentCaptor<ChatDTO> saved = ArgumentCaptor.forClass(ChatDTO.class);
		verify(chatService, times(1)).insertChat(saved.capture());
		ChatDTO sv = saved.getValue();
		org.junit.Assert.assertEquals("안녕?", sv.getQuestion());
		org.junit.Assert.assertEquals("안녕하세요!", sv.getAnswer());
		org.junit.Assert.assertEquals("sess-1", sv.getSessionId());
		org.junit.Assert.assertEquals(100, sv.getUserNo().intValue());
	}

	@Disabled
	@Test
	public void send_badRequest_empty() throws Exception {
		ChatDTO req = new ChatDTO(null, 100, "sess-1", "   ", null, null);

		mockMvc.perform(post("/api/chat/send").characterEncoding("UTF-8").contentType(APP_JSON_UTF8)
				.content(om.writeValueAsString(req))).andExpect(status().isBadRequest());
	}

	@Disabled
	@Test
	public void send_withBot_errorMessage_passthrough() throws Exception {
		ChatDTO req = new ChatDTO(null, 101, "sess-2", "요청이 많아?", null, null);

		when(botService.reply(any(String.class), any(String.class), any(Integer.class)))
				.thenReturn("요청이 많습니다. 잠시 후 다시 시도해 주세요.");
		when(chatService.insertChat(any(ChatDTO.class))).thenReturn(1);

		mockMvc.perform(post("/api/chat/send").characterEncoding("UTF-8").contentType(APP_JSON_UTF8)
				.header("X-Session-Id", "sess-2").content(om.writeValueAsString(req))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.answer").value("요청이 많습니다. 잠시 후 다시 시도해 주세요."));

		verify(botService).reply(eq("요청이 많아?"), eq("sess-2"), eq(101));
		verify(chatService).insertChat(any(ChatDTO.class));
	}

	@Disabled
	@Test
	public void get_one_found_and_notFound() throws Exception {
		ChatDTO found = new ChatDTO(10L, 100, "sess-1", "Q", "A", "2025-08-08 12:00:00");
		when(chatService.selectChat(10L)).thenReturn(found);
		when(chatService.selectChat(999L)).thenReturn(null);

		mockMvc.perform(get("/api/chat/10").characterEncoding("UTF-8")).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.logNo").value(10));

		mockMvc.perform(get("/api/chat/999").characterEncoding("UTF-8")).andExpect(status().isNotFound());
	}

	// @Disabled
	@Test
	public void list_ok() throws Exception {
		SearchDTO search = new SearchDTO(1, 1, "10", "안녕");
		ChatDTO row = new ChatDTO(1L, 100, "sess-1", "안녕", "안녕하세요!", "2025-08-08 12:00:00");

		when(chatService.chatList(any(SearchDTO.class))).thenReturn(Arrays.asList(row));

		mockMvc.perform(post("/api/chat/list").characterEncoding("UTF-8").contentType(APP_JSON_UTF8)
				.content(om.writeValueAsString(search))).andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

		verify(chatService, times(1)).chatList(any(SearchDTO.class));
	}

	@Disabled
	@Test
	public void delete_ok_and_notFound() throws Exception {
		when(chatService.deleteChat(1L)).thenReturn(1);
		when(chatService.deleteChat(2L)).thenReturn(0);

		mockMvc.perform(delete("/api/chat/1").characterEncoding("UTF-8")).andExpect(status().isOk())
				.andExpect(content().string("삭제 성공"));

		mockMvc.perform(delete("/api/chat/2").characterEncoding("UTF-8")).andExpect(status().isNotFound())
				.andExpect(content().string("삭제 실패"));

		verify(chatService, times(1)).deleteChat(1L);
		verify(chatService, times(1)).deleteChat(2L);
	}
}