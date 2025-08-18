package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.DustDTO;
import com.pcwk.ehr.mapper.DustMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class DustDaoTest {

	Logger log = LogManager.getLogger(getClass());

	@Autowired
	ApplicationContext context;

	@Autowired
	DustMapper mapper;

	// ---------------------------------------
	// 공통 유틸
	// ---------------------------------------
	private static boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}

	@BeforeEach
	void setUp() {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
	}

	@AfterEach
	void tearDown() {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}

	@Test
	@DisplayName("스프링 빈 주입 확인")
	void beans() {
		assertNotNull(context, "ApplicationContext 주입 실패");
		assertNotNull(mapper, "DustMapper 주입 실패");
		log.debug("context: {}", context);
		log.debug("mapper : {}", mapper);
	}

	// ------------------------------------------------------------
	// 1) BBox + 대기유형 최신 데이터 조회 (마커용)
	// mapper.selectLatestByTypeBBox(airType, day, minLat, maxLat, minLon, maxLon,
	// limit)
	// ------------------------------------------------------------
	@Test
	@DisplayName("BBox+유형 최신 조회 OK")
	void selectLatestByTypeBBox_ok() {
		String airType = "도로변대기"; // 프로젝트 내 실제 값으로 조정

		// 한반도 대략 범위
		Double minLat = 33.0, maxLat = 38.7;
		Double minLon = 124.5, maxLon = 131.0;
		Integer limit = 300;

		List<DustDTO> rows = mapper.selectLatestByTypeBBox(airType, null, minLat, maxLat, minLon, maxLon, limit);

		assertNotNull(rows, "rows must not be null");
		log.debug("rows.size={}", rows.size());

		for (DustDTO r : rows) {
			assertFalse(isBlank(r.getStnNm()), "stnNm must not be blank");
			assertTrue(r.getLat() >= minLat && r.getLat() <= maxLat, "lat out of bbox: " + r.getLat());
			assertTrue(r.getLon() >= minLon && r.getLon() <= maxLon, "lon out of bbox: " + r.getLon());
			if (!isBlank(airType)) {
				assertEquals(airType, r.getOrg(), "org(대기유형) 불일치");
			}
		}
	}

	// ------------------------------------------------------------
	// 4) 특정 유형 전체 최신 조회
	// mapper.selectLatestByTypeAll(airType, day, limit)
	// ------------------------------------------------------------
	@Test
	@DisplayName("유형별 최신 전체 조회 OK")
	void selectLatestByTypeAll_ok() {
		String airType = "도시대기";
		List<DustDTO> rows = mapper.selectLatestByTypeAll(airType, null, 300);

		assertNotNull(rows, "rows must not be null");
		log.debug("rows.size={}", rows.size());

		for (DustDTO r : rows) {
			assertFalse(isBlank(r.getStnNm()), "stnNm must not be blank");
			if (!isBlank(airType)) {
				assertEquals(airType, r.getOrg(), "org(대기유형) 불일치");
			}
		}
	}

}
