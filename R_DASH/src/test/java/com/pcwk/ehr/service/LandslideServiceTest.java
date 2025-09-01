package com.pcwk.ehr.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.LandslideDTO;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
  "file:src/main/webapp/WEB-INF/spring/root-context.xml",
  "file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class LandslideServiceTest {

    final Logger log = LogManager.getLogger(getClass());

    @Autowired
    LandslideService service;

    @Test
    void selectByBBox_ok() {
        double minLat = 33.0, maxLat = 39.5, minLon = 124.0, maxLon = 132.0;
        List<LandslideDTO> rows = service.selectByBBox(minLat, maxLat, minLon, maxLon, null, null);
        assertNotNull(rows);
        log.info("service selectByBBox size={}", rows.size());
    }

    @Test
    void countByRegionInBBox_ok() {
        double minLat = 33.0, maxLat = 39.5, minLon = 124.0, maxLon = 132.0;
        List<Map<String,Object>> rows = service.countByRegionInBBox(minLat, maxLat, minLon, maxLon, null, null);
        assertNotNull(rows);
        log.info("service countByRegionInBBox size={}", rows.size());
    }

    @Test
    void countBySidoInBBox_ok() {
        double minLat = 33.0, maxLat = 39.5, minLon = 124.0, maxLon = 132.0;
        List<Map<String,Object>> rows = service.countBySidoInBBox(minLat, maxLat, minLon, maxLon, null, null);
        assertNotNull(rows);
        log.info("service countBySidoInBBox size={}", rows.size());
    }

   
}
