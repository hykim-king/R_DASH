package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

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


    @Test
    @DisplayName("selectByBBox: 리스트 반환 & 좌표 필수값 확인")
    void selectByBBox_ok() {
        List<LandslideDTO> list = mapper.selectByBBox(MIN_LAT, MAX_LAT, MIN_LON, MAX_LON, null);
        assertNotNull(list);
        log.debug("selectByBBox size={}", list.size());
        if (!list.isEmpty()) {
            LandslideDTO e = list.get(0);
            assertNotNull(e.getLat(), "lat null");
            assertNotNull(e.getLon(), "lon null");
        }
    }

    @Test
    @DisplayName("findById: 단건 상세 NPE 없이 동작")
    void findById_safe() {
        LandslideDTO dto = mapper.findById(1L); // 실제 PK 알면 변경
        log.debug("findById(1) -> {}", dto);
        // 실PK 확정 시:
        // assertNotNull(dto);
        // assertEquals(1, dto.getLandslideNo());
    }

    @Test
    @DisplayName("countByRegionInBBox: 시군구 집계 구조 검증")
    void countByRegionInBBox_ok() {
        List<Map<String, Object>> agg = mapper.countByRegionInBBox(MIN_LAT, MAX_LAT, MIN_LON, MAX_LON, null);
        assertNotNull(agg);
        log.debug("countByRegionInBBox size={}", agg.size());
        if (!agg.isEmpty()) {
            Map<String,Object> m = agg.get(0);
            assertTrue(m.containsKey("lat"));
            assertTrue(m.containsKey("lon"));
            assertTrue(m.containsKey("cnt"));
        }
    }

    @Test
    @DisplayName("countBySidoInBBox: 시/도 집계 구조 검증")
    void countBySidoInBBox_ok() {
        List<Map<String, Object>> agg = mapper.countBySidoInBBox(MIN_LAT, MAX_LAT, MIN_LON, MAX_LON, null);
        assertNotNull(agg);
        log.debug("countBySidoInBBox size={}", agg.size());
        if (!agg.isEmpty()) {
            Map<String,Object> m = agg.get(0);
            assertTrue(m.containsKey("lat"));
            assertTrue(m.containsKey("lon"));
            assertTrue(m.containsKey("cnt"));
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
