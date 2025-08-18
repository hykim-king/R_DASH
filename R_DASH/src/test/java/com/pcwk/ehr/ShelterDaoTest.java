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

import com.pcwk.ehr.domain.ShelterDTO;
import com.pcwk.ehr.mapper.ShelterMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
class ShelterDaoTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	ShelterMapper mapper;

	@Autowired
	ApplicationContext context;

	@BeforeEach
	public void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");

	}

	@AfterEach
	public void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}

	@Test
	void selectByBBox_basic() {
		double minLat = 37.45, maxLat = 37.60;
		double minLon = 126.80, maxLon = 127.10;

		List<ShelterDTO> rows = mapper.selectByBBox(minLat, maxLat, minLon, maxLon, null, 100);
		assertNotNull(rows, "rows must not be null");
		log.info("rows size={}", rows.size());
	}

	@Test
	void selectOne_basic() {
		ShelterDTO dto = mapper.selectOne(1L); // PK=1 테스트
		log.info("selectOne(1) -> {}", dto);
		assertTrue(true); // 실행 확인용
	}

	@Test
//	@Disabled
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
