package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class LandslideControllerTest {

	Logger log = LogManager.getLogger(getClass());

	@Autowired
	LandslideController controller;

	@Autowired
	ApplicationContext context;

	 // 대한민국 대략 BBox
    double minLat = 33.0, maxLat = 39.5, minLon = 124.0, maxLon = 132.0;

    @BeforeEach
    void setUp() {
        assertNotNull(context, "Spring ApplicationContext 주입 실패");
        assertNotNull(controller, "LandslideController 빈 주입 실패");
    }

    @Test
    @DisplayName("bubbles(level=sgg): 시군구 버블 집계 JSON 객체 반환")
    void bubbles_sgg_ok() {
        List<Map<String, Object>> res = controller.bubbles(minLat, maxLat, minLon, maxLon, "sgg", null);
        assertNotNull(res);
        log.debug("bubbles(sgg) size={}", res.size());
        if (!res.isEmpty()) {
            Map<String, Object> m = res.get(0);
            assertTrue(m.containsKey("cnt"));
            assertTrue(m.containsKey("lat"));
            assertTrue(m.containsKey("lon"));
        }
    }

    @Test
    @DisplayName("bubbles(level=sido): 시/도 버블 집계 JSON 객체 반환")
    void bubbles_sido_ok() {
        List<Map<String, Object>> res = controller.bubbles(minLat, maxLat, minLon, maxLon, "sido", null);
        assertNotNull(res);
        log.debug("bubbles(sido) size={}", res.size());
        if (!res.isEmpty()) {
            Map<String, Object> m = res.get(0);
            assertTrue(m.containsKey("cnt"));
            assertTrue(m.containsKey("lat"));
            assertTrue(m.containsKey("lon"));
            assertTrue(m.containsKey("sidoNm"));
        }
    }

    @Test
    @DisplayName("points: BBox 내 포인트 목록 반환")
    void points_ok() {
        List<LandslideDTO> res = controller.points(minLat, maxLat, minLon, maxLon, null);
        assertNotNull(res);
        log.debug("points size={}", res.size());
        if (!res.isEmpty()) {
            LandslideDTO e = res.get(0);
            assertNotNull(e.getLat(), "lat null?");
            assertNotNull(e.getLon(), "lon null?");
        }
    }

    @Test
    @DisplayName("detail: 단건 상세 (실PK 알면 강한 검증 추가)")
    void detail_safe() {
        LandslideDTO dto = controller.detail(1L); // 실제 PK로 변경 가능
        log.debug("detail(1) -> {}", dto);
        // 실데이터가 확실하면 아래 주석 해제
        // assertNotNull(dto);
        // assertEquals(1L, dto.getLandslideNo());
    }

}