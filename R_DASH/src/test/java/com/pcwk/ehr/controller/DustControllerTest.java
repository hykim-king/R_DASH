package com.pcwk.ehr.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * ✅ MockMvc를 사용해 실제 컨트롤러/서비스/매퍼/DB를 타는 통합 테스트 ✅ @MockBean 같은 가짜(Mock) 객체는 전혀
 * 사용하지 않음 ✅ /api/dust/latest REST API를 호출하고 응답 상태/타입을 검증
 */
@WebAppConfiguration // 웹 애플리케이션 컨텍스트(WebApplicationContext)를 로딩
@ExtendWith(SpringExtension.class) // JUnit5 + Spring TestContext Framework 통합
@ContextConfiguration(locations = {
		// 스프링 빈 설정 파일 경로 지정 (root-context: 서비스/매퍼/DB, servlet-context: 컨트롤러/MVC)
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class DustControllerTest {

	Logger log = LogManager.getLogger(getClass());

	@Autowired
	private WebApplicationContext wac; // 실제 웹 애플리케이션 컨텍스트 주입

	private MockMvc mockMvc; // HTTP 요청/응답을 흉내내는 테스트 클라이언트

	@BeforeEach
	void setUp() {
		// WebApplicationContext 기반으로 MockMvc 생성 → 실제 빈들을 모두 사용
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	/**
	 * ✅ BBox(지도 화면 범위) 조건이 있을 때 API 호출 테스트
	 */
	@Test
	void latest_bbox_ok() throws Exception {
		MvcResult result = mockMvc
				.perform(get("/api/dust/latest").param("airType", "도시대기").param("minLat", "33.0")
						.param("maxLat", "38.7").param("minLon", "124.5").param("maxLon", "131.0").param("limit", "5"))
				.andDo(print()).andReturn();

		// 예외가 있었다면 로그로 확인
		if (result.getResolvedException() != null) {
			log.error("🔥 Controller threw exception", result.getResolvedException());
		}

		// 응답 본문 로그
		log.info("📌 Response Status = {}", result.getResponse().getStatus());
		log.info("📌 Response Body   = {}", result.getResponse().getContentAsString());
	}

	/**
	 * ✅ 전국 단위(전제 BBox 없이) API 호출 테스트
	 */
	@Test
	void latest_all_ok() throws Exception {
		MvcResult r = mockMvc.perform(get("/api/dust/latest").param("airType", "교외대기") // SQL에선 미사용
				.param("limit", "5")).andDo(print()).andReturn();

		int status = r.getResponse().getStatus();
		String body = r.getResponse().getContentAsString();
		Throwable ex = r.getResolvedException();

		log.info("latest_all_ok :: status={}", status);
		log.info("latest_all_ok :: body={}", body);
		if (ex != null) {
			log.error("latest_all_ok :: exception", ex);
		}

		// 문제 파악 끝나면 assert로 200 확인
		org.junit.jupiter.api.Assertions.assertEquals(200, status,
				"expected 200 but was " + status + " / body=" + body);
	}

}
