package com.pcwk.ehr.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
public class ShelterControllerTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	WebApplicationContext wac;

	MockMvc mockMvc;

	@BeforeEach
	void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	/** bbox API 테스트 */
	@Test
	void bboxEndpoint_ok() throws Exception {
		mockMvc.perform(get("/api/shelters/bbox").param("minLat", "37.45").param("maxLat", "37.60")
				.param("minLon", "126.80").param("maxLon", "127.10")).andExpect(status().isOk());
	}

	/** 단건 조회 API 테스트 */
	@Test
	void oneEndpoint_ok() throws Exception {
		mockMvc.perform(get("/api/shelters/1")).andExpect(status().isOk());
	}
}
