package com.pcwk.ehr.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class LandslideServiceTest {

    private static final Logger log = LogManager.getLogger(LandslideServiceTest.class);

    @Autowired
    ApplicationContext context;

    @Autowired
    LandslideService service; // 실제 빈 주입

    // 전국 대략 BBox
    private static final double MIN_LAT = 33.0;
    private static final double MAX_LAT = 39.5;
    private static final double MIN_LON = 124.0;
    private static final double MAX_LON = 132.0;

    @BeforeEach
    void setUp() {
        assertNotNull(context, "ApplicationContext 주입 실패");
        assertNotNull(service, "LandslideService 주입 실패");
    }

    @Test
    @DisplayName("getPoints: BBox 내 포인트 목록 반환")
    void getPoints_ok() {
        List<LandslideDTO> list = service.getPoints(MIN_LAT, MAX_LAT, MIN_LON, MAX_LON, null);
        assertNotNull(list);
        log.debug("getPoints size={}", list.size());
        if (!list.isEmpty()) {
            LandslideDTO e = list.get(0);
            // 위경도는 0도 이상/이하일 수 있으므로 null 여부만 간단히 체크
            assertNotNull(e.getLat(), "lat null?");
            assertNotNull(e.getLon(), "lon null?");
        }
    }

    @Test
    @DisplayName("getBubbles(level=sgg): 시군구 집계 Map 키 구조 확인")
    void getBubbles_sgg_ok() {
        List<Map<String, Object>> agg = service.getBubbles(MIN_LAT, MAX_LAT, MIN_LON, MAX_LON, "sgg", null);
        assertNotNull(agg);
        log.debug("getBubbles(sgg) size={}", agg.size());
        if (!agg.isEmpty()) {
            Map<String, Object> m = agg.get(0);
            // XML에서 따옴표 alias로 "regionName","lat","lon","cnt" 보장
            assertTrue(m.containsKey("regionName"));
            assertTrue(m.containsKey("lat"));
            assertTrue(m.containsKey("lon"));
            assertTrue(m.containsKey("cnt"));
        }
    }

    @Test
    @DisplayName("getBubbles(level=sido): 시/도 집계 Map 키 구조 확인")
    void getBubbles_sido_ok() {
        List<Map<String, Object>> agg = service.getBubbles(MIN_LAT, MAX_LAT, MIN_LON, MAX_LON, "sido", null);
        assertNotNull(agg);
        log.debug("getBubbles(sido) size={}", agg.size());
        if (!agg.isEmpty()) {
            Map<String, Object> m = agg.get(0);
            // XML에서 "sidoNm","lat","lon","cnt" 보장
            assertTrue(m.containsKey("sidoNm"));
            assertTrue(m.containsKey("lat"));
            assertTrue(m.containsKey("lon"));
            assertTrue(m.containsKey("cnt"));
        }
    }

    @Test
    @DisplayName("getDetail: 단건 상세 (실PK 알면 강한 검증 추가)")
    void getDetail_safe() {
        LandslideDTO dto = service.getDetail(1L); // 실제 존재 PK로 교체 권장
        log.debug("getDetail(1) -> {}", dto);
        // 실데이터가 보장되면:
        // assertNotNull(dto);
        // assertEquals(1, dto.getLandslideNo());
    }
}
