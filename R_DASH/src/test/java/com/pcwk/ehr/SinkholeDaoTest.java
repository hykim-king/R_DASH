//package com.pcwk.ehr;
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
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import com.pcwk.ehr.domain.SinkholeDTO;
//import com.pcwk.ehr.mapper.SinkholeMapper;
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(locations = {
//    "file:src/main/webapp/WEB-INF/spring/root-context.xml"
//})
//
//class SinkholeDaoTest {
//
//    private final Logger log = LogManager.getLogger(getClass());
//
//    @Autowired
//    private SinkholeMapper mapper;
//
//    @Autowired
//    private ApplicationContext context;
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
//    @Test
//    @DisplayName("컨텍스트/빈 주입 확인")
//    void beans() {
//        assertNotNull(mapper);
//        assertNotNull(context);
//        log.debug("mapper: {}", mapper);
//        log.debug("context: {}", context);
//    }
//
//    @Test
//    @DisplayName("selectAll(): 전체 목록 조회")
//    void selectAllTest() {
//        List<SinkholeDTO> list = mapper.selectAll();
//        assertNotNull(list);
//        log.debug("조회 건수 = {}", list.size());
//        // 데이터가 보장되면 다음 라인도 활성화
//        // assertTrue(list.size() > 0);
//    }
//
//    @Test
//    @DisplayName("findById(): 단건 조회 (예: 1001)")
//    void findOneTest() {
//        // 픽스처가 없다면 존재하는 PK로 바꾸세요.
//        SinkholeDTO dto = mapper.findById(1001);
//        // 픽스처 없으면 아래는 느슨하게:
//        // assertTrue(dto == null || dto.getSinkholeNo() == 1001);
//        // 픽스처가 있다면:
//        // assertNotNull(dto);
//        // assertEquals(1001, dto.getSinkholeNo());
//        log.debug("findById(1001) -> {}", dto);
//    }
//
//    @Test
//    @DisplayName("selectByBBox(): BBOX 내 조회")
//    void selectByBBoxTest() {
//        double minLat = 35.0, maxLat = 38.0, minLon = 126.0, maxLon = 129.5;
//        List<SinkholeDTO> list = mapper.selectByBBox(minLat, maxLat, minLon, maxLon);
//        assertNotNull(list);
//        log.debug("BBOX 조회 건수 = {}", list.size());
//        // 좌표 범위 검증(옵션)
//        list.forEach(r -> {
//            assertTrue(r.getLat() >= minLat && r.getLat() <= maxLat);
//            assertTrue(r.getLon() >= minLon && r.getLon() <= maxLon);
//        });
//    }
//}
