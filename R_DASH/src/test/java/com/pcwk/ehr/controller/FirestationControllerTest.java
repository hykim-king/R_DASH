package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


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

	Logger log = LogManager.getLogger(getClass());

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        assertNotNull(mockMvc);
    }

    @Test
    void bbox_shouldReturn200AndJsonArray() throws Exception {
        mockMvc.perform(
                get("/api/firestations/bbox")
                        .param("minLat", "33.0")
                        .param("maxLat", "38.5")
                        .param("minLon", "124.0")
                        .param("maxLon", "132.0")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void search_shouldReturnPageEnvelope() throws Exception {
        mockMvc.perform(
                get("/api/firestations/search")
                        .param("q", "소방")
                        .param("orderBy", "name")
                        .param("limit", "5")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows").exists())
                .andExpect(jsonPath("$.total").exists());
    }

    @Test
    void one_shouldReturnDetail() throws Exception {
        // 실제 PK를 모르면, 먼저 검색해서 얻어온 id로 테스트하는 통합 시나리오를 별도 작성해도 좋음.
        // 여기서는 일단 1번을 조회(없으면 404 대신 200 + null을 반환하는 구조라면 json 본문만 체크).
        mockMvc.perform(get("/api/firestations/{stationNo}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void autocompleteArea_shouldWork() throws Exception {
        mockMvc.perform(get("/api/firestations/auto/area")
                        .param("prefix", "서")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void autocompleteStation_shouldWork() throws Exception {
        mockMvc.perform(get("/api/firestations/auto/station")
                        .param("prefix", "강")
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

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
