package com.pcwk.ehr.controller;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import com.pcwk.ehr.domain.DustDTO;

/**
 * ✅ 순수 스프링 컨텍스트 + 컨트롤러 직접 호출 (No Mock, No Spring Boot, No MockMvc)
 * - 실 서비스/매퍼/DB를 사용
 */
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class DustControllerTest {

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private DustController controller; // 컨트롤러 빈 직접 주입

    @BeforeEach
    void setUp() {
        assertNotNull(controller, "DustController 주입 실패: 컴포넌트 스캔/빈 등록 확인");
    }

    @Test
    @DisplayName("전국 조회: airType만으로 최신 데이터 반환")
    void latest_all_ok() {
        String airType = "도시대기"; // ORG 컬럼 값과 정확히 일치해야 함 (도시대기/도로변대기/교외대기)
        Integer limit = 5;

        List<DustDTO> rows = controller.latest(airType, null, null, null, null, null, limit);

        assertNotNull(rows, "latest() 결과 null");
        assertTrue(rows.size() <= limit, "limit 초과 반환");
        if (!rows.isEmpty()) {
            DustDTO d = rows.get(0);
            assertNotNull(d.getStnNm(), "측정소명(stnNm) null");
            assertEquals(airType, d.getOrg(), "ORG(대기종류) 불일치");
            log.info("latest_all_ok -> size={}, first={}", rows.size(), d);
        }
    }

    @Test
    @DisplayName("BBox 조회: 화면 영역 내 최신 데이터 반환")
    void latest_bbox_ok() {
        String airType = "도로변대기";
        String day = null; // 필요 시 "2025-08-05" 등 존재하는 날짜 지정
        double minLat = 33.0, maxLat = 38.7, minLon = 124.5, maxLon = 131.0;
        int limit = 5;

        List<DustDTO> rows = controller.latest(
            airType, day, minLat, maxLat, minLon, maxLon, limit
        );

        assertNotNull(rows, "latest(BBox) 결과 null");
        assertTrue(rows.size() <= limit, "limit 초과 반환");
        if (!rows.isEmpty()) {
            DustDTO d = rows.get(0);
            assertTrue(d.getLat() >= minLat && d.getLat() <= maxLat, "위도 범위 벗어남");
            assertTrue(d.getLon() >= minLon && d.getLon() <= maxLon, "경도 범위 벗어남");
            assertEquals(airType, d.getOrg(), "ORG(대기종류) 불일치");
            log.info("latest_bbox_ok -> size={}, first={}", rows.size(), d);
        }
    }

    @Test
    @DisplayName("특정 일자 + airType 조회: 최신 데이터 반환")
    void latest_airtype_with_day_ok() {
        String airType = "교외대기";
        String day = "2025-08-05"; // DB에 존재하는 날짜로 조정 가능
        int limit = 5;

        List<DustDTO> rows = controller.latest(airType, day, null, null, null, null, limit);

        assertNotNull(rows, "latest(day) 결과 null");
        assertTrue(rows.size() <= limit, "limit 초과 반환");
        // 데이터가 없을 수도 있으니 배열 여부만 확인
        log.info("latest_airtype_with_day_ok -> size={}", rows.size());
    }

    @Test
    @DisplayName("통계: PM10 Top5/Bottom5/Avg")
    void stats_endpoints_ok() {
        List<Map<String, Object>> top5 = controller.getTop5PM10();
        List<Map<String, Object>> bottom5 = controller.getBottom5PM10();

        assertNotNull(top5, "top5 null");
        assertNotNull(bottom5, "bottom5 null");
        log.info("stats -> top5={}, bottom5={}", top5.size(), bottom5.size());
    }

    @Test
    @DisplayName("뷰 라우팅: /dust/statsPage")
    void statsPage_view_ok() throws Exception {
        Model model = new ConcurrentModel();
        String view = controller.statsPage(model);

        assertEquals("stats/statsMain", view, "뷰 이름 불일치");
        assertEquals("dust", model.asMap().get("pageType"), "pageType 모델 값 불일치");
    }
}
