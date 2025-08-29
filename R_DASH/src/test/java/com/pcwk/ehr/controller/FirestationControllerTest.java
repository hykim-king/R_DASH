//package com.pcwk.ehr.controller;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//import java.util.Map;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import com.pcwk.ehr.domain.FirestationDTO;
//
///**
// * 순수 스프링 컨텍스트 + 컨트롤러 직접 호출 통합 테스트
// * - MockMvc/Mockito/Boot 미사용
// * - 실제 service/mapper/DB까지 연결됨
// */
//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(locations = {
//    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
//    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
//})
//class FirestationControllerTest {
//
//    private final Logger log = LogManager.getLogger(getClass());
//
//    @Autowired
//    private FirestationController controller;
//
//    @BeforeEach
//    void setUp() {
//        assertNotNull(controller, "FirestationController 주입 실패 → 컴포넌트 스캔/빈 등록 확인");
//    }
//
//    @Test
//    @DisplayName("BBox: 화면 영역 내 소방서/안전센터 목록 (fireTp=ALL)")
//    void bbox_shouldReturnList_ALL() {
//        double minLat = 33.0, maxLat = 38.5, minLon = 124.0, maxLon = 132.0;
//        String q = null;
//        Integer limit = 5;
//        String fireTp = "ALL";
//
//        // 컨트롤러 시그니처: bbox(minLat,maxLat,minLon,maxLon,q,limit,fireTp)
//        List<FirestationDTO> rows = controller.bbox(minLat, maxLat, minLon, maxLon, q, limit, fireTp);
//
//        assertNotNull(rows, "bbox() 결과 null");
//        assertTrue(rows.size() <= limit, "limit 초과 반환");
//        if (!rows.isEmpty()) {
//            FirestationDTO f = rows.get(0);
//            assertNotNull(f.getStationNo(), "stationNo는 null이면 안됨");
//            assertTrue(f.getLat() >= minLat && f.getLat() <= maxLat, "위도 범위 벗어남");
//            assertTrue(f.getLon() >= minLon && f.getLon() <= maxLon, "경도 범위 벗어남");
//            assertNotNull(f.getFireTp(), "fireTp 매핑 누락");
//        }
//        log.info("bbox(ALL) -> size={}", rows.size());
//    }
//
//    @Test
//    @DisplayName("검색: rows/total/limit/offset 포함 (fireTp=SAFETY)")
//    void search_shouldReturnPageEnvelope_SAFETY() {
//        String q = "소방"; // 없으면 null 가능
//        String area = null;
//        Double minLat = null, maxLat = null, minLon = null, maxLon = null;
//        String orderBy = "name";
//        Integer limit = 5, offset = 0;
//        String fireTp = "SAFETY";
//
//        // 컨트롤러 시그니처: search(..., orderBy, limit, offset, fireTp)
//        Map<String, Object> page = controller.search(
//                q, area, minLat, maxLat, minLon, maxLon, orderBy, limit, offset, fireTp
//        );
//
//        assertNotNull(page, "search() 결과 null");
//        assertTrue(page.containsKey("rows"), "rows 키 없음");
//        assertTrue(page.containsKey("total"), "total 키 없음");
//        assertEquals(limit, page.get("limit"), "limit 불일치");
//        assertEquals(offset, page.get("offset"), "offset 불일치");
//
//        @SuppressWarnings("unchecked")
//        List<FirestationDTO> rows = (List<FirestationDTO>) page.get("rows");
//        assertNotNull(rows, "rows null");
//        assertTrue(rows.size() <= limit, "limit 초과 반환");
//        if (!rows.isEmpty()) {
//            assertEquals("SAFETY", rows.get(0).getFireTp(), "fireTp 필터 불일치");
//        }
//        log.info("search(SAFETY) -> rows.size={}, total={}", rows.size(), page.get("total"));
//    }
//
//    @Test
//    @DisplayName("단건 상세: 존재하면 필드 검증")
//    void one_shouldReturnDetail() {
//        int stationNo = 1; // 테스트 환경에 맞게 조정
//        FirestationDTO dto = controller.one(stationNo);
//
//        assertNotNull(dto, "상세 조회 null → 테스트용 PK를 실제 존재하는 값으로 맞춰주세요");
//        if (dto != null) {
//            assertEquals(stationNo, dto.getStationNo().intValue(), "stationNo 불일치");
//            assertNotNull(dto.getStationNm(), "stationNm null");
//        }
//        log.info("one -> {}", dto);
//    }
//
//    @Test
//    @DisplayName("자동완성: 지역명 (fireTp=ALL)")
//    void autoArea_shouldWork_ALL() {
//        String prefix = "서";
//        Integer limit = 5;
//        String fireTp = "ALL";
//
//        // 컨트롤러 시그니처: autoArea(prefix, limit, fireTp)
//        List<String> list = controller.autoArea(prefix, limit, fireTp);
//
//        assertNotNull(list, "autoArea 결과 null");
//        assertTrue(list.size() <= limit, "limit 초과");
//        if (!list.isEmpty()) {
//            // 정책에 따라 startsWith가 아닐 수 있음 → 느슨 검증
//            assertTrue(list.get(0) != null && list.get(0).length() > 0);
//        }
//        log.info("autoArea(ALL) -> size={}, first={}", list.size(), list.isEmpty()?null:list.get(0));
//    }
//
//    @Test
//    @DisplayName("자동완성: 소방서명 (fireTp=ALL)")
//    void autoStation_shouldWork_ALL() {
//        String prefix = "강";
//        String area = null; // 필요 시 지역 지정
//        Integer limit = 5;
//        String fireTp = "ALL";
//
//        // 컨트롤러 시그니처: autoStation(prefix, area, limit, fireTp)
//        List<String> list = controller.autoStation(prefix, area, limit, fireTp);
//
//        assertNotNull(list, "autoStation 결과 null");
//        assertTrue(list.size() <= limit, "limit 초과");
//        if (!list.isEmpty()) {
//            assertTrue(list.get(0) != null && list.get(0).length() > 0);
//        }
//        log.info("autoStation(ALL) -> size={}, first={}", list.size(), list.isEmpty()?null:list.get(0));
//    }
//}
