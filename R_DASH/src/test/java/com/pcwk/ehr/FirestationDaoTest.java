package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.FirestationDTO;
import com.pcwk.ehr.mapper.FirestationMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { 
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" 
})
class FirestationDaoTest {
    Logger log = LogManager.getLogger(getClass());

    @Autowired
    FirestationMapper mapper;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    public void setUp() throws Exception {
        log.debug("┌────────────────────┐");
        log.debug("│ setUp()            │");
        log.debug("└────────────────────┘");
    }

    @AfterEach
    public void tearDown() throws Exception {
        log.debug("┌────────────────────┐");
        log.debug("│ tearDown()         │");
        log.debug("└────────────────────┘");
    }

    
    
    // ------------------- 테스트 -------------------

    /** BBox + 키워드 */
    @Test
    public void selectByBBoxTest() {
        double minLat = 37.0;
        double maxLat = 38.0;
        double minLon = 126.0;
        double maxLon = 127.0;
        String q = "서울"; // 키워드 없으면 null 가능

        List<FirestationDTO> list = mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q, 20);
        assertNotNull(list);
        log.debug("BBox 검색 건수: {}", list.size());
        list.forEach(log::debug);

        // 위경도 범위 체크
        list.forEach(dto -> {
            assertTrue(dto.getLat() >= minLat && dto.getLat() <= maxLat);
            assertTrue(dto.getLon() >= minLon && dto.getLon() <= maxLon);
        });
    }

    /** 검색 (지역, 키워드, 페이징) */
    @Test
    public void searchTest() {
        List<FirestationDTO> list = mapper.search("소방", "서울특별시",
                37.0, 38.0, 126.0, 127.0,
                "name", 10, 0);
        assertNotNull(list);
        log.debug("검색 결과 건수: {}", list.size());
        list.forEach(log::debug);
    }

    /** 검색 총 건수 */
    @Test
    public void countSearchTest() {
        int total = mapper.countSearch("소방", "서울특별시",
                37.0, 38.0, 126.0, 127.0);
        log.debug("검색 총 건수: {}", total);
        assertTrue(total >= 0);
    }

    /** 단건 상세 */
    @Test
    public void selectOneTest() {
        // 테스트용으로 PK=1 조회 (데이터 없으면 null일 수 있음)
        FirestationDTO dto = mapper.selectOne(1);
        log.debug("단건 조회 결과: {}", dto);
        assertNotNull(dto);
    }

    /** 자동완성 - 지역 */
    @Test
    public void autocompleteAreaTest() {
        List<String> list = mapper.autocompleteArea("서", 5);
        assertNotNull(list);
        log.debug("지역 자동완성 결과: {}", list);
    }

    /** 자동완성 - 소방서명 */
    @Test
    public void autocompleteStationTest() {
        List<String> list = mapper.autocompleteStation("중부", "서울특별시", 5);

        assertNotNull(list);
        log.debug("소방서명 자동완성 결과: {}", list);
    }

    /** 스프링 빈 체크 */
    @Test
    void beans() {
        assertNotNull(mapper);
        assertNotNull(context);
        log.debug("mapper: {}", mapper);
        log.debug("context: {}", context);
    }
}
