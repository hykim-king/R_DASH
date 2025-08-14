package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

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

import com.pcwk.ehr.domain.LandslideDTO;
import com.pcwk.ehr.mapper.LandslideMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class LandslideDaoTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	LandslideMapper  mapper;

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
	
	
//	 selectByBBox 쿼리 결과 검증 – 지도 화면(BBox) 내 산사태 목록 조회
//	 @Disabled
	 @Test
	    void selectByBBox_latest() {
		 //지도에서 현재 보이는 범위(대한민국 전체 범위 예시)
	        double minLat = 33.0, maxLat = 39.0;
	        double minLon = 124.0, maxLon = 132.0;
	        String q = null; // 또는 "경보", "주의보" 등
	        
//	        Mapper 호출  : BBox범위 내 산사태 목록 조회
	        List<LandslideDTO> rows = mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q);
	        log.debug("┌────────────────────────────────┐");
			log.debug("│ selectByBBox_latest()          │");
			log.debug("└────────────────────────────────┘");
			
			
//			결과 리스트가 null이 아닌지 확인
	        assertNotNull(rows, "rows must not be null");
//	                 결과 건수 로그 출력
	        log.info("rows.size={}", rows.size());
	        
	        //데이터가 있다면 첫 번째 레코드 상세 출력
	        if (!rows.isEmpty()) {
	            LandslideDTO first = rows.get(0);
	            log.info("first: no={}, inst={}, dt={}, stts={}, lat={}, lon={}",
	                    first.getLandslideNo(), first.getLndInstNm(), first.getLndApntDt(),
	                    first.getLndApntStts(), first.getLat(), first.getLon());
	        }
	        assertTrue(rows.size() >= 0);
	    }
	 
	 
// 		findById 쿼리 결과 검증 – ** PK로 단건 상세 조회 ** 
//		@Disabled
	    @Test
	    void findById_ok() {
	        Long testId = 1L; // 실제 DB에 존재하는 PK로 변경
	        LandslideDTO dto = mapper.findById(testId);
	        log.debug("┌────────────────────────┐");
			log.debug("│ findById_ok()          │");
			log.debug("└────────────────────────┘");
			
			
	        assertNotNull(dto, "dto must not be null");
	        log.info("dto: {}", dto);
	    }

//	  	countByRegionInBBox 쿼리 결과 검증 – ** BBox 내 지역별 발생 건수 집계 **
//	    @Disabled
	    @Test
	    void countByRegionInBBox_ok() {
	        double minLat = 33.0, maxLat = 39.0;
	        double minLon = 124.0, maxLon = 132.0;
	        String q = ""; // 키워드 없으면 빈문자

	        List<Map<String, Object>> agg = mapper.countByRegionInBBox(minLat, maxLat, minLon, maxLon, q);
	        log.debug("┌────────────────────────────────────┐");
			log.debug("│ countByRegionInBBox_ok()           │");
			log.debug("└────────────────────────────────────┘");
			
			
			
	        assertNotNull(agg, "agg must not be null");
	        log.info("agg.size={}", agg.size());
	        if (!agg.isEmpty()) {
	            Map<String, Object> top = agg.get(0);
	            log.info("top region={}, cnt={}, latestDt={}",
	                    top.get("region"), top.get("cnt"), top.get("latestDt"));
	        }
	        assertTrue(agg.size() >= 0);
	    }
	
	
//	@Disabled
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
