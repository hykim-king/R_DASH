package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.FirestationDTO;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class FirestationControllerTest {

    final Logger log = LogManager.getLogger(getClass());

    @Autowired
    ApplicationContext context;

    @Autowired
    FirestationController controller;

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

    // --------------------------------- 테스트 ---------------------------------

    /** 검색 총 건수 (GET /api/firestations/countSearch) */
    @Test
    void countSearch_shouldReturnNonNegative() {
        ResponseEntity<Integer> res = controller.countSearch(
                "소방", "서울특별시",
                37.0, 38.0, 126.0, 127.0
        );
        assertNotNull(res);
        assertNotNull(res.getBody());
        assertTrue(res.getBody() >= 0, "총 건수는 0 이상이어야 함");
        log.debug("총 건수: {}", res.getBody());
    }

    /** BBox 조회 (GET /api/firestations/bbox) */
    @Test
    void bbox_shouldReturnList() {
        List<FirestationDTO> list = controller.bbox(
                37.0, 38.0,   // minLat, maxLat
                126.0, 127.0, // minLon, maxLon
                "서울",       // q (nullable)
                10            // limit (nullable)
        );
        assertNotNull(list);
        log.debug("BBox 결과 건수: {}", list.size());
        list.forEach(it -> log.debug("{}", it));
    }

    /** 검색 + 페이징 + 정렬 + BBox (GET /api/firestations/search) */
    @SuppressWarnings("unchecked")
    @Test
    void search_shouldReturnEnvelope() {
        Map<String, Object> envelope = controller.search(
                "소방", "서울특별시",
                37.0, 38.0, 126.0, 127.0,
                "stationNm", 10, 0
        );

        assertNotNull(envelope, "envelope가 null이면 안됨");
        assertTrue(envelope.containsKey("rows"));
        assertTrue(envelope.containsKey("total"));
        assertTrue(envelope.containsKey("limit"));
        assertTrue(envelope.containsKey("offset"));

        List<FirestationDTO> rows = (List<FirestationDTO>) envelope.get("rows");
        Integer total = (Integer) envelope.get("total");
        Integer limit = (Integer) envelope.get("limit");
        Integer offset = (Integer) envelope.get("offset");

        assertNotNull(rows);
        assertNotNull(total);
        assertNotNull(limit);
        assertNotNull(offset);
        assertTrue(total >= 0);
        assertTrue(limit > 0);
        assertTrue(offset >= 0);

        log.debug("검색 rows.size={}, total={}, limit={}, offset={}",
                rows.size(), total, limit, offset);
    }

    /** 단건 상세 (GET /api/firestations/{stationNo}) */
    @Test
    void one_shouldReturnDto() {
        FirestationDTO dto = controller.one(1); // PK=1 가정
        assertNotNull(dto, "단건 조회 결과 null이면 안됨");
        log.debug("단건 조회: {}", dto);
    }

    /** 자동완성 - 지역 (GET /api/firestations/auto/area) */
    @Test
    void autoArea_shouldReturnList() {
        List<String> areas = controller.autoArea("대구", 5);
        assertNotNull(areas);
        log.debug("지역 자동완성: {}", areas);
    }

    /** 자동완성 - 소방서명 (GET /api/firestations/auto/station) */
    @Test
    void autoStation_shouldReturnList() {
        List<String> stationNm = controller.autoStation("수성소방서", 5);
        assertNotNull(stationNm);
        log.debug("소방서명 자동완성: {}", stationNm);
    }

    /** 스프링 빈 주입 확인 */
    @Test
    void beans() {
        assertNotNull(context);
        assertNotNull(controller);
        log.debug("context: {}", context);
        log.debug("controller: {}", controller);
    }
}
