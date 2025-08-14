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

	// 실제 테스트할 날짜 (테이블에 존재하는 데이터 날짜여야 함)
	private static final String DAY = "2025-08-05";

	// 문자열이 null이거나 빈칸인지 체크하는 간단 유틸
	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
	
	

//  1. 전국 모든 측정소의 하루 평균 미세먼지 데이터 조회 테스트
//	@Disabled
	@Test
	void selectStationAvgAt10_all() {
		// 파라미터: 날짜, 위도/경도 null → 전국 전체 조회
		List<DustDTO> rows = mapper.selectStationAvgAt10(DAY, null, null, null, null);
		log.debug("┌────────────────────────────────────┐");
		log.debug("│ selectStationAvgAt10_all()         │");
		log.debug("└────────────────────────────────────┘");
		assertNotNull(rows, "rows must not be null"); // NULL 체크

		for (DustDTO r : rows) {
			// stnNm 비어있지 않은지
			assertFalse(isBlank(r.getStnNm()), "stnNm must not be blank");
			// 위경도 범위 체크
			assertTrue(r.getLat() >= 33.0 && r.getLat() <= 39.5, "lat out of KR bounds: " + r.getLat());
			assertTrue(r.getLon() >= 124.0 && r.getLon() <= 132.0, "lon out of KR bounds: " + r.getLon());
		}
	}

// 2. BBox(지도 영역) 내 측정소 평균 데이터 조회 테스트
//	@Disabled
	@Test
	void selectStationAvgAt10_bbox() {
		Double minLat = 37.0, maxLat = 37.8, minLon = 126.5, maxLon = 127.4;

		List<DustDTO> rows = mapper.selectStationAvgAt10(DAY, minLat, minLon, maxLat, maxLon);
		log.debug("┌────────────────────────────────────┐");
		log.debug("│ selectStationAvgAt10_bbox()        │");
		log.debug("└────────────────────────────────────┘");
		assertNotNull(rows, "rows must not be null");
		for (DustDTO r : rows) {
			assertTrue(r.getLat() >= minLat && r.getLat() <= maxLat, "lat not in bbox: " + r.getLat());
			assertTrue(r.getLon() >= minLon && r.getLon() <= maxLon, "lon not in bbox: " + r.getLon());
		}
	}

//	3. ORG(대기 유형)별 최신 데이터 조회 테스트
//	대기 측정소 데이터 중 특정 ORG(대기 유형 = 도시대기) 값만
//	@Disabled
	@Test
	void selectLatestByOrgAt10_city() {
		String org = "도시대기";
		Double minLat = 34.0, maxLat = 38.7, minLon = 126.0, maxLon = 129.6;

		// ✅ 정적 호출(X) -> 주입받은 mapper 인스턴스로 호출(O)
		List<DustDTO> rows = mapper.selectLatestByOrgAt10(DAY, org, minLat, minLon, maxLat, maxLon);
		log.debug("┌────────────────────────────────────────┐");
		log.debug("│ selectLatestByOrgAt10_city()           │");
		log.debug("└────────────────────────────────────────┘");
		log.info("LatestByOrg(도시대기) count = {}", rows == null ? null : rows.size());

		if (rows != null) {
			rows.forEach(r -> log.debug("Station: {}, org={}, lat={}, lon={}, avg={}", r.getStnNm(), r.getOrg(),
					r.getLat(), r.getLon(), r.getAvg()));
		}

		assertNotNull(rows, "rows must not be null");

		for (DustDTO r : rows) {
			assertEquals(org, r.getOrg(), "org must match");

			// ❌ assertNotNull(r.getLat()) / r.getLon() 는 double 원시형이라 무의미
			// ✅ 범위 검증만 수행
			assertTrue(r.getLat() >= minLat && r.getLat() <= maxLat, "lat not in bbox: " + r.getLat());
			assertTrue(r.getLon() >= minLon && r.getLon() <= maxLon, "lon not in bbox: " + r.getLon());
		}
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
