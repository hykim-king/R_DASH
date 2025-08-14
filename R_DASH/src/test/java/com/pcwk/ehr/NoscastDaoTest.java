package com.pcwk.ehr;

// ★ JUnit5 assertions
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

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

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.mapper.NowcastMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class NoscastDaoTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	NowcastMapper mapper;

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
	void wiring() {
		assertNotNull(mapper, "Mapper 주입 실패");
	}

	@Test
	void selectNowcastByRegion_latest() {
		// 1) 최신 공통 시각
		Map<String, String> latest = mapper.selectLatestCommonBase();
		String baseDate = latest.get("BASE_DATE");
		String baseTime = latest.get("BASE_TIME");

		assertNotNull(baseDate, "BASE_DATE null");
		assertNotNull(baseTime, "BASE_TIME null");

		// 2) 실제 DB에 존재하는 값으로 변경하세요
		String sidoNm = "서울특별시";
		String signguNm = "강남구";

		// 3) 조회
		List<NowcastDTO> list = mapper.selectNowcastByRegion(baseDate, baseTime, sidoNm, signguNm);
		log.debug("┌────────────────────────────┐");
		log.debug("│ selectNowcastByRegion()    │");
		log.debug("└────────────────────────────┘");

		for (NowcastDTO dto : list) {
			System.out.printf("%s=%s%n", dto.getCategory(), dto.getObsrValue());
		}
		// 4) 검증
		assertNotNull(list, "조회 결과가 null");
		assertTrue(list.size() > 0, "결과가 비었습니다.");

		boolean hasT1H = false, hasRN1 = false, hasWSD = false, hasREH = false;

		for (NowcastDTO r : list) {
			assertEquals(sidoNm, r.getSidoNm(), "SIDO_NM 불일치");
			// ★ 게터 이름 확인: DTO 필드가 signguNm 라면 getSignguNm() 사용
			assertEquals(signguNm, r.getSignguNm(), "SIGNGU_NM 불일치");

			switch (r.getCategory()) {
			case "T1H":			// 기온
				hasT1H = true;   
				break;
			case "RN1":			// 강수량
				hasRN1 = true;
				break;
			case "WSD":			// 풍속
				hasWSD = true;
				break;
			case "REH":			// 습도
				hasREH = true;
				break;
			}
		}

		assertTrue(hasT1H, "T1H 없음");
		assertTrue(hasRN1, "RN1 없음");
		assertTrue(hasWSD, "WSD 없음");
		assertTrue(hasREH, "REH 없음");

		log.debug("OK: {} {} ({}, {}) -> {} rows", sidoNm, signguNm, baseDate, baseTime, list.size());
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
