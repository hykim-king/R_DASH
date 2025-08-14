package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;           // ← JUnit5 Test
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.domain.MapDTO;          // ← DTO import
import com.pcwk.ehr.mapper.MapMapper; 

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class MapDaoTest {

	Logger log = LogManager.getLogger(getClass());

	@Autowired
	MapMapper  mapper;

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
	
//	 @Disabled
	 @Test
	    void bbox_sinkhole_ok() {
	        List<MapDTO> rows = mapper.selectMarkersByBBox(   // ← MapMapper가 아니라 mapper
	            "SINKHOLE",
	            33.0, 38.7, 124.5, 132.0,
	            "",                 // q
	            "2024-01-01",       // dateFrom
	            "2025-12-31",       // dateTo
	            200                 // limit
	        );
	        assertNotNull(rows, "rows must not be null");
	        log.info("SINKHOLE rows={}", rows.size());
	        assertTrue(rows.size() >= 0);
	    }

	 
//	 @Disabled
	    @Test
	    void bbox_shelter_keyword_ok() {
	        List<MapDTO> rows = mapper.selectMarkersByBBox(
	            "SHELTER",
	            35.0, 38.0, 126.0, 129.0,
	            "강원특별자치도 영월군 영월읍 날골길 36",  //RONA_DADDR - 도로명 주소
	            null, null,
	            100
	        );
	        assertNotNull(rows);
	        log.info("SHELTER rows={}", rows.size());
	        assertTrue(rows.size() >= 0);
	    }

	 
	 
//	 @Disabled
	    @Test
	    void detail_landslide_ok() {
	        MapDTO dto = mapper.findDetail("LANDSLIDE", 1);   // ← 인스턴스 호출
	        log.info("LANDSLIDE detail={}", dto);
	    }
	    
//	    @Disabled
	    @Test
	    void bbox_fire_station_ok() {
	        int limit = 100;
	        List<MapDTO> rows = mapper.selectMarkersByBBox(
	                "FIRE_STATION",
	                35.0, 38.0, 126.0, 129.0,
	                "",   // q
	                null, null,
	                limit
	        );
	        log.info("FIRE_STATION rows={}", rows.size());
	        assertRowsWithinBBox(rows, 35.0, 38.0, 126.0, 129.0, "FIRE_STATION", limit);
	    }

//	    @Disabled
	    @Test
	    void bbox_dust_ok() {
	        int limit = 150;
	        double minLat = 34.0, maxLat = 38.5, minLon = 125.0, maxLon = 129.5;

	        List<MapDTO> rows = mapper.selectMarkersByBBox(
	                "DUST",
	                minLat, maxLat, minLon, maxLon,
	                "",          // q
	                null, null,  // date
	                limit
	        );

	        log.info("DUST rows={}", rows.size());
	        assertRowsWithinBBox(rows, minLat, maxLat, minLon, maxLon, "DUST", limit);

	        // DUST는 최신 1건/측정소로 구성 — 발생일시가 보통 존재
	        for (MapDTO r : rows) {
	            assertNotNull(r.getOccurDt(), "DUST occurDt must not be null");
	        }
	    }
	    
	 // ==== 공통 헬퍼 ====
	    private void assertRowsWithinBBox(List<MapDTO> rows,
	                                      double minLat, double maxLat,
	                                      double minLon, double maxLon,
	                                      String layer, int limit) {
	        assertNotNull(rows, "rows must not be null");
	        assertTrue(rows.size() <= limit, "size must be <= limit");

	        for (MapDTO r : rows) {
	            // 레이어명 일치
	            assertTrue(layer.equalsIgnoreCase(r.getTableNm()),
	                    "tableNm mismatch: expected=" + layer + ", got=" + r.getTableNm());

	            // BBox 안에 들어오는지
	            assertNotNull(r.getLat(), "lat must not be null");
	            assertNotNull(r.getLon(), "lon must not be null");
	            assertTrue(r.getLat() >= minLat && r.getLat() <= maxLat,
	                    "lat out of bbox: " + r.getLat());
	            assertTrue(r.getLon() >= minLon && r.getLon() <= maxLon,
	                    "lon out of bbox: " + r.getLon());
	        }
	    }



	    
}
