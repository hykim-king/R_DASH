package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.SinkholeDTO;
import com.pcwk.ehr.mapper.SinkholeMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class SinkholeDaoTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	SinkholeMapper mapper;

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
	
	
	
	/** BBox 기본 조회 (대한민국 전체 범위) */
//	@Disabled
    @Test
    void selectByBBox_allKorea() {
        double minLat = 33.0, maxLat = 39.5;
        double minLon = 124.0, maxLon = 132.0;

        List<SinkholeDTO> rows = mapper.selectByBBox(
                minLat, maxLat, minLon, maxLon,
                null, null, null
        );

        assertNotNull(rows, "rows must not be null");
        log.debug("rows.size={}", rows.size());
        if (!rows.isEmpty()) {
            SinkholeDTO first = rows.get(0);
            log.debug("first: no={}, addr={} {}, pos={}/{}, state={}, occurDt={}",
                    first.getSinkholeNo(), //시퀀스
                    first.getSidoNm(), first.getSignguNm(), // 발생 지역
                    first.getLat(), first.getLon(),			//위경도
                    first.getStateNm(), first.getOccurDt());//복구현황 및 발생 일시
        }
        assertTrue(rows.size() >= 0);
    }

    /** BBox + 키워드 + 날짜 조건 */
//	@Disabled
    @Test
    void selectByBBox_withKeywordAndDate() {
        double minLat = 35.0, maxLat = 38.0;
        double minLon = 126.0, maxLon = 129.0;

        List<SinkholeDTO> rows = mapper.selectByBBox(
                minLat, maxLat, minLon, maxLon,
                "성남",            // 키워드 (null/"" 가능)
                "2010-01-01",     // from (YYYY-MM-DD)
                "2025-08-14"      // to   (YYYY-MM-DD, 당일 포함)
        );

        assertNotNull(rows, "rows must not be null");
        log.debug("cond rows.size={}", rows.size());
        assertTrue(rows.size() >= 0);
    }


	
	
	@Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");

		assertNotNull(mapper);
		assertNotNull(context);

		log.debug("mapper: {}" + mapper);
		log.debug("context: {}" + context);
	}
}
