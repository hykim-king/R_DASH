package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
	DustMapper mapper;

	@Autowired
	ApplicationContext context;

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
	void selectLatestByTypeBBox_ok() {
		String airType = "도로변대기"; // 프로젝트에서 사용하는 실제 값으로 변경
		String day = null; // 또는 "2025-08-17"

		// 한반도 대략 범위(예시)
		Double minLat = 33.0, maxLat = 38.7;
		Double minLon = 124.5, maxLon = 131.0;

		List<DustDTO> rows = mapper.selectLatestByTypeBBox(airType, day, minLat, maxLat, minLon, maxLon, 300);

		assertNotNull(rows);
		log.debug("rows.size={}", rows.size());
		if (!rows.isEmpty()) {
			log.debug("first={}", rows.get(0));
		}
		assertTrue(rows != null); // NPE 방지용 확인
	}

	@Test
	void selectLatestByTypeAll_ok() {
		String airType = "도시대기";
		List<DustDTO> rows = mapper.selectLatestByTypeAll(airType, null, 300);
		assertNotNull(rows);
		log.debug("rows.size={}", rows.size());
		assertTrue(rows != null);
	}

//	@Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");

		assertNotNull(mapper);
		assertNotNull(context);

		log.debug("mapper: {}" + mapper);
		log.debug("context: {}" + context);
	}

}
