package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class) // JUnit5 + Spring 연동
@WebAppConfiguration // WebApplicationContext 로드
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"
})
class MainControllerTest {

	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	WebApplicationContext webApplicationContext;

	MockMvc mockMvc;

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}

	//@Disabled
	@Test
	void testHome() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ testHome() 실행    │");
		log.debug("└────────────────────┘");

		mockMvc.perform(get("/home"))
				.andExpect(status().isOk())
				.andExpect(view().name("main/main"))
				.andExpect(model().attributeExists("msg"))
				.andExpect(model().attribute("msg", "저희 재난 알림 사이트를 방문해주셔서 감사합니다."));
	}
	
	//@Disabled
	@Test
	void testSearch() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ testSearch() 실행  │");
		log.debug("└────────────────────┘");

		String keyword = "지진";

		mockMvc.perform(get("/search").param("keyword", keyword))
				.andExpect(status().isOk())
				.andExpect(view().name("searchResults"))
				.andExpect(model().attributeExists("keyword"))
				.andExpect(model().attribute("keyword", keyword))
				.andExpect(model().attributeExists("results"));
	}
	
	
	//@Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");
		
		assertNotNull(webApplicationContext);
		assertNotNull(mockMvc);
		
		
		log.debug("webApplicationContext: {}"+webApplicationContext);
		log.debug("mockMvc: {}"+mockMvc);
		
	}

}
