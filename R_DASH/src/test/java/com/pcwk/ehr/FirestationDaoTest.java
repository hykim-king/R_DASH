//package com.pcwk.ehr;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.web.WebAppConfiguration;
//
//import com.pcwk.ehr.domain.FirestationDTO;
//import com.pcwk.ehr.mapper.FirestationMapper;
//
//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(locations = { 
//    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
//    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" 
//})
//class FirestationDaoTest {
//    Logger log = LogManager.getLogger(getClass());
//
//    @Autowired
//    FirestationMapper mapper;
//
//    @Autowired
//    ApplicationContext context;
//
//    @BeforeEach
//    void setUp() {
//        log.debug("┌────────────────────┐");
//        log.debug("│ setUp()            │");
//        log.debug("└────────────────────┘");
//    }
//
//    @AfterEach
//    void tearDown() {
//        log.debug("┌────────────────────┐");
//        log.debug("│ tearDown()         │");
//        log.debug("└────────────────────┘");
//    }
//
//    // ------------------- 테스트 -------------------
//
//    /** BBox + ALL */
//    @Test
//    void selectByBBox_ALL_returnsPoints() {
//        List<FirestationDTO> rows = mapper.selectByBBox(
//                35.0, 38.0, 126.0, 128.0,
//                null, 1000, "ALL"
//        );
//        assertNotNull(rows);
//        assertTrue(rows.size() > 0);
//        assertNotNull(rows.get(0).getFireTp());
//    }
//
//    /** SAFETY만 필터 */
//    @Test
//    void search_filterBySAFETY() {
//        List<FirestationDTO> rows = mapper.search(
//                null, null,
//                35.0, 38.0, 126.0, 129.5,
//                "name", 50, 0, "SAFETY"
//        );
//        assertNotNull(rows);
//        for (FirestationDTO dto : rows) {
//            assertEquals("SAFETY", dto.getFireTp());
//        }
//    }
//
//    /** countSearch에서 fireTp 조건 반영 확인 */
//    @Test
//    void countSearch_respectsFireTp() {
//        int all = mapper.countSearch(null, null, 35.0, 38.0, 126.0, 129.5, "ALL");
//        int mainOnly = mapper.countSearch(null, null, 35.0, 38.0, 126.0, 129.5, "MAIN");
//        assertTrue(all >= mainOnly);
//    }
//
//    /** 단건 상세 조회 */
//    @Test
//    void selectOne_returnsDto() {
//        FirestationDTO dto = mapper.selectOne(1);
//        log.debug("단건 조회 결과: {}", dto);
//        assertNotNull(dto);
//        if (dto != null) {
//            log.debug("stationNm={}", dto.getStationNm());
//        }
//    }
//
//    /** 자동완성 - 지역 */
//    @Test
//    void autocompleteAreaTest() {
//        List<String> list = mapper.autocompleteArea("서", 5, "ALL"); // fireTp 인자 추가
//        assertNotNull(list);
//        log.debug("지역 자동완성 결과: {}", list);
//    }
//
//    /** 자동완성 - 소방서명 */
//    @Test
//    void autocompleteStationTest() {
//        List<String> list = mapper.autocompleteStation("중부", "서울특별시", 5, "ALL"); // fireTp 인자 추가
//        assertNotNull(list);
//        log.debug("소방서명 자동완성 결과: {}", list);
//    }    /** 스프링 빈 체크 */
//    @Test
//    void beans() {
//        assertNotNull(mapper);
//        assertNotNull(context);
//        log.debug("mapper: {}", mapper);
//        log.debug("context: {}", context);
//    }
//}
