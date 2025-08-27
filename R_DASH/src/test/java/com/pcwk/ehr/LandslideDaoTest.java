package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

import com.pcwk.ehr.domain.LandslideDTO;
import com.pcwk.ehr.mapper.LandslideMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class LandslideDaoTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	LandslideMapper mapper;

	@Autowired
	ApplicationContext context;


	
    // ✅ 전역 상수로 선언 (대한민국 대략 BBox)
    private static final double MIN_LAT = 33.0;
    private static final double MAX_LAT = 39.5;
    private static final double MIN_LON = 124.0;
    private static final double MAX_LON = 132.0;
    
    
	@BeforeEach
	public void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");

		assertNotNull(context, "Spring ApplicationContext 주입 실패");
		assertNotNull(mapper, "LandslideMapper 빈 주입 실패");

	}

	@AfterEach
	public void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}


    // 지도 포인트 조회
    @Test
    void selectByBBox_ok() {
        double minLat = 33.0, maxLat = 39.5, minLon = 124.0, maxLon = 132.0;
        List<LandslideDTO> rows = mapper.selectByBBox(minLat, maxLat, minLon, maxLon, null, null);
        assertNotNull(rows);
        log.info("selectByBBox size={}", rows.size());
    }

    // 히트맵(지역 집계)
    @Test
    void countByRegionInBBox_ok() {
        double minLat = 33.0, maxLat = 39.5, minLon = 124.0, maxLon = 132.0;
        List<Map<String,Object>> rows = mapper.countByRegionInBBox(minLat, maxLat, minLon, maxLon, null, null);
        assertNotNull(rows);
        log.info("countByRegionInBBox size={}", rows.size());
    }

    // 시도 집계
    @Test
    void countBySidoInBBox_ok() {
        double minLat = 33.0, maxLat = 39.5, minLon = 124.0, maxLon = 132.0;
        List<Map<String,Object>> rows = mapper.countBySidoInBBox(minLat, maxLat, minLon, maxLon, null, null);
        assertNotNull(rows);
        log.info("countBySidoInBBox size={}", rows.size());
    }

    // 단건
    @Test
    void findById_ok() {
        // 샘플 PK가 확실치 않으니 존재 가능성만 체크 (null 허용)
        Long anyId = 1L;
        LandslideDTO dto = mapper.findById(anyId);
        log.info("findById({}) -> {}", anyId, (dto!=null ? "HIT" : "null"));
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
