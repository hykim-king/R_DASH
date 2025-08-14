package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
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

//	전체 목록 조회
//	@Disabled
	@Test
	public void selectAllFfsTest() {
		List<FirestationDTO> list = mapper.selectAll();
		log.debug("┌────────────────────┐");
		log.debug("│ selectAllTest()    │");
		log.debug("└────────────────────┘");
		
		assertNotNull(list);
		assertTrue(list.size() >= 0);
		log.debug("전체 목록 건수: {}", list.size());
		list.forEach(log::debug);
	}
	
	

//	단건 조회
//	@Disabled
	@Test
	public void findByIdfsTest() {
		int testId = 1; 
		FirestationDTO dto = mapper.findById(testId);
		log.debug("┌────────────────────────┐");
		log.debug("│ findByIdTest()         │");
		log.debug("└────────────────────────┘");
		
		
		assertNotNull(dto);
		log.debug("단건 조회: {}", dto);
	}


//	지도 BBox + 키워드 기반 소방서 목록 회회
//	@Disabled
	@Test
	public void selectByBBoxfsTest() {
		double minLat = 37.0;
		double maxLat = 38.0;
		double minLon = 126.0;
		double maxLon = 127.0;
		String q = "서울"; // 키워드 없으면 null 가능

		List<FirestationDTO> list = mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q);
		log.debug("┌────────────────────────────┐");
		log.debug("│ selectByBBoxTest()         │");
		log.debug("└────────────────────────────┘");
		
		
		assertNotNull(list);
		log.debug("BBox 검색 건수: {}", list.size());
		list.forEach(log::debug);
	}
	
	
	

	
//	검색 시 자동 완성
//	** 필수 기능 아님 ( 나중 구현 )**
//	필수 기능 아님 ( 추후 구현 )
//	@Disabled
	@Test
	public void suggestKeywordfsTest() {
		String q = "서울";
		List<String> list = mapper.suggestKeyword(q);
		log.debug("┌────────────────────┐");
		log.debug("│ suggestKeywordTest()         │");
		log.debug("└────────────────────┘");
		
		
		assertNotNull(list);
		log.debug("지역 자동완성 결과: {}", list);
	}


//	검색 시 자동 완성
//	** 필수 기능 아님 ( 나중 구현 )**
//	필수 기능 아님 ( 나중 구현 )
//	@Disabled
	@Test
	public void suggestStationfsTest() {
		String q = "중부";
		List<String> list = mapper.suggestStation(q);
		log.debug("┌──────────────────────────────┐");
		log.debug("│ suggestStationTest()         │");
		log.debug("└──────────────────────────────┘");
		
		
		assertNotNull(list);
		log.debug("소방서명 자동완성 결과: {}", list);
	}
	
	
	

//	지역명 기준 소방서 검색
//	** 필수 기능 아님 ( 나중 구현 )**
//	필수 기능 아님 ( 나중 구현 )
//	@Disabled
	@Test
	public void listByAreafsTest() {
		String area = "서울";
		List<FirestationDTO> list = mapper.listByArea(area);
		log.debug("┌────────────────────┐");
		log.debug("│ listByAreaTest()   │");
		log.debug("└────────────────────┘");
		
		
		assertNotNull(list);
		log.debug("지역 기준 목록 건수: {}", list.size());
		list.forEach(log::debug);
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
