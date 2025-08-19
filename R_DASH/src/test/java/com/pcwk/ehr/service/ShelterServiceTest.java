package com.pcwk.ehr.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.ShelterDTO;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class ShelterServiceTest {

    final Logger log = LogManager.getLogger(getClass());

    @Autowired
    ApplicationContext context;

    @Autowired
    ShelterService service;  // 실제 Service Bean 주입 (Mapper/DB도 연결됨)

    @BeforeEach
    void setUp() {
        log.debug("context={}, service={}", context, service);
    }

    /** 단건 조회 */
    @Test
    void selectOne_ok() throws Exception {
        Integer shelterNo = 1; // 실제 DB에 1번 데이터가 있어야 성공
        ShelterDTO result = service.selectOne(shelterNo);

        assertNotNull(result, "결과가 null이면 안됨");
        log.info("selectOne({}) -> {}", shelterNo, result);
    }

    /** BBox 조회 */
    @Test
    void selectByBBox_ok() throws Exception {
        double minLat = 37.4, maxLat = 37.6;
        double minLon = 126.8, maxLon = 127.1;

        List<ShelterDTO> rows = service.selectByBBox(minLat, maxLat, minLon, maxLon, null, 10);

        assertNotNull(rows);
        assertFalse(rows.isEmpty(), "BBox 결과가 비어있음");
        log.info("BBox 결과 건수={}", rows.size());
        rows.forEach(r -> log.info("{}", r));
    }

    /** 자동완성 - 주소 */
    @Test
    void suggestAdress_ok() throws Exception {
        List<String> list = service.suggestAdress("서울");

        assertNotNull(list);
        assertFalse(list.isEmpty(), "자동완성 결과 없음");
        assertTrue(list.get(0).contains("서울"));
        log.info("자동완성 결과 -> {}", list);
    }
}
