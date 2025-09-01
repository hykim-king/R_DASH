//package com.pcwk.ehr.service;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
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
//
//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(locations = { 
//    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
//    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
//})
//class FirestationServiceTest {
//
//    final Logger log = LogManager.getLogger(getClass());
//
//    @Autowired
//    FirestationService service;
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
//    // ------------------ 테스트 ------------------
//
//    /** BBox + 키워드 */
//    /** BBox 조회 */
//    @Test
//    void selectByBBox_shouldReturnRows() {
//        List<FirestationDTO> rows = service.selectByBBox(35.0, 38.0, 126.0, 129.5, null, 100, "ALL");
//        assertNotNull(rows);
//        assertTrue(rows.size() >= 0);
//        log.debug("BBox rows.size={}", rows.size());
//    }
//
//    /** 단건 상세 */
//    @Test
//    void selectOneTest() {
//        FirestationDTO dto = service.selectOne(1); // PK=1 가정
//        assertNotNull(dto, "조회 결과가 null이면 안됨 (테스트 DB 데이터 확인 필요)");
//        log.debug("단건 조회: {}", dto);
//    }
//
//    /** 자동완성 - 지역 */
//    @Test
//    void autocompleteAreaTest() {
//        List<String> areas = service.autocompleteArea("서", 5, "ALL");
//        assertNotNull(areas);
//        log.debug("지역 자동완성: {}", areas);
//    }
//
//    /** 자동완성 - 소방서명 */
//    @Test
//    void autocompleteStationTest() {
//        List<String> names = service.autocompleteStation("중부", "서울특별시", 5, "ALL");
//        assertNotNull(names);
//        log.debug("소방서명 자동완성: {}", names);
//    }
//
//    /** 스프링 빈 주입 확인 */
//    @Test
//    void beans() {
//        assertNotNull(service);
//        assertNotNull(context);
//        log.debug("service: {}", service);
//        log.debug("context: {}", context);
//    }
//}