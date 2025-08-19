package com.pcwk.ehr.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.DustDTO;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;


@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/root-context.xml"		
									,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"})
public class DustServiceTest {
	 Logger log = LogManager.getLogger(getClass());

	    @Autowired
	    ApplicationContext context;

	    @Autowired
	    DustService dustService;

	    @Test
	    void latestByTypeBBox_ok() {
	        // given
	        String airType = "도시대기";  // 프로젝트에서 실제 사용하는 값으로 변경
	        String day = null;          // "YYYY-MM-DD" 가능
	        Double minLat = 33.0, maxLat = 38.7;
	        Double minLon = 124.5, maxLon = 131.0;
	        Integer limit = 300;

	        // when
	        List<DustDTO> rows = dustService.getLatestByTypeBBox(
	                airType, day, minLat, maxLat, minLon, maxLon, limit);

	        // then
	        assertNotNull(rows, "서비스 결과가 null이면 안됨");
	        log.debug("BBox rows.size={}", rows.size());
	        if (!rows.isEmpty()) log.debug("first={}", rows.get(0));
	        assertTrue(rows != null); // NPE 방지 검증(데이터 유무와 무관)
	    }

	    @Test
	    void latestByTypeAll_ok() {
	        // given
	        String airType = "도로변대기";
	        Integer limit = 200;

	        // when
	        List<DustDTO> rows = dustService.getLatestByTypeAll(airType, null, limit);

	        // then
	        assertNotNull(rows, "서비스 결과가 null이면 안됨");
	        log.debug("ALL rows.size={}", rows.size());
	        assertTrue(rows != null);
	    }
	}

