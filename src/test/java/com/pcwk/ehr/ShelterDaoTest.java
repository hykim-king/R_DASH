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

import com.pcwk.ehr.domain.ShelterDTO;
import com.pcwk.ehr.mapper.ShelterMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
class ShelterDaoTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	ShelterMapper mapper;

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
	@Disabled
	@Test
	public void selectAllFsTest() {

	public void selectAllFfsTest() {

		List<ShelterDTO> list = mapper.selectAll();
		log.debug("┌────────────────────┐");
		log.debug("│ selectAllTest()    │");
		log.debug("└────────────────────┘");

		assertNotNull(list);
		assertTrue(list.size() >= 0);
		list.forEach(log::debug);
		log.debug("전체 목록 건수: {}", list.size());
	}

//	단건 조회
	@Disabled
	@Test
	public void findByIdsTest() {
		int testId = 1;
		ShelterDTO dto = mapper.findById(testId);
		log.debug("┌────────────────────────┐");
		log.debug("│ findByIdTest()         │");
		log.debug("└────────────────────────┘");

		assertNotNull(dto);
		log.debug("단건 조회: {}", dto);

	}



//	지역 기준 목록 ( 나중 구현 )
	@Disabled
	@Test
	public void listByAreasTest() {
		String ronaDaddr = "서울";
		List<ShelterDTO> list = mapper.listByArea(ronaDaddr);
		log.debug("┌────────────────────┐");
		log.debug("│ listByAreaTest()   │");
		log.debug("└────────────────────┘");

		assertNotNull(list);
		log.debug("지역 기준 목록 건수: {}", list.size());
		list.forEach(log::debug);
	}

//	필수 기능 아님 ( 추후 구현 )
//	@Disabled
	@Test
	public void suggestKeywordsTest() {
		String q = "서울";
		List<String> list = mapper.suggestKeyword(q);
		log.debug("┌────────────────────────────────┐");
		log.debug("│ suggestKeywordTest()           │");
		log.debug("└────────────────────────────────┘");

		assertNotNull(list);
		log.debug("지역 자동완성 결과: {}", list);
	}

	// BBox + 키워드 조회
	// BBox 검색 건수 ( 지정된 지역 & 위경도 범위 내에 있는 데이터만 조회 )
//	@Disabled


//	지역 기준 목록 ( 나중 구현 )
	@Disabled
	@Test
	public void listByAreafsTest() {
		String ronaDaddr = "서울";
		List<ShelterDTO> list = mapper.listByArea(ronaDaddr);
		log.debug("┌────────────────────┐");
		log.debug("│ listByAreaTest()   │");
		log.debug("└────────────────────┘");

		assertNotNull(list);
		log.debug("지역 기준 목록 건수: {}", list.size());
		list.forEach(log::debug);
	}

//	필수 기능 아님 ( 추후 구현 )
//	@Disabled
	@Test
	public void suggestKeywordsTest() {
		String q = "서울";
		List<String> list = mapper.suggestKeyword(q);
		log.debug("┌────────────────────────────────┐");
		log.debug("│ suggestKeywordTest()           │");
		log.debug("└────────────────────────────────┘");

		assertNotNull(list);
		log.debug("지역 자동완성 결과: {}", list);
	}

	// BBox + 키워드 조회
	// BBox 검색 건수 ( 지정된 지역 & 위경도 범위 내에 있는 데이터만 조회 )
//	@Disabled
	@Test
	public void selectByBBoxsTest() {
		double minLat = 37.0;
		double maxLat = 38.0;
		double minLon = 126.0;
		double maxLon = 127.0;
		String q = "서울"; // 키워드 없으면 null 가능

		List<ShelterDTO> list = mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q);
		log.debug("┌────────────────────────────┐");
		log.debug("│ selectByBBoxTest()         │");
		log.debug("└────────────────────────────┘");

		assertNotNull(list);
		log.debug("BBox 검색 건수: {}", list.size());
		list.forEach(log::debug);
	}



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
