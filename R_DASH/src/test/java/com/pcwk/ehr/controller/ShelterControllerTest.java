package com.pcwk.ehr.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.pcwk.ehr.service.ShelterService;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
    "file:src/main/webapp/WEB-INF/spring/root-context.xml",
    "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
public class ShelterControllerTest {

    Logger log = LogManager.getLogger(getClass());

    @Autowired
    ApplicationContext context;

    @Autowired
    ShelterService shelterService; // Controller 말고 Service 직접 호출

    @BeforeEach
    void setup() {
        log.debug("setup(): context={}, service={}", context, shelterService);
    }

    /** bbox API → 실제 서비스 호출 */
    @Test
    void bboxService_ok() throws Exception {
        double minLat = 37.45, maxLat = 37.60;
        double minLon = 126.80, maxLon = 127.10;

        List<ShelterDTO> rows = shelterService.selectByBBox(minLat, maxLat, minLon, maxLon, null, 100);

        assertNotNull(rows);
        log.info("bbox 결과 건수={}", rows.size());
        rows.forEach(r -> log.info("{}", r));
    }

    /** 단건 조회 API → 실제 서비스 호출 */
    @Test
    void oneService_ok() throws Exception {
        Integer shelterNo = 1; // PK=1 레코드가 실제 DB에 있어야 함
        ShelterDTO dto = shelterService.selectOne(shelterNo);

        assertNotNull(dto, "조회 결과가 null이면 안됨");
        log.info("selectOne({}) -> {}", shelterNo, dto);
    }
}
