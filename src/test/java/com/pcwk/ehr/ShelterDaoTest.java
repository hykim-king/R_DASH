package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
	
	
//	단건 조회
//	@Disabled
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
	

	@Test
	public void selectByBBoxsTest() {
		double minLat = 34.0;
		double maxLat = 38.0;
		double minLon = 126.0;
		double maxLon = 127.0;
		String q = "한파쉼터";
		
		List<ShelterDTO> list = mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q);
		log.debug("┌────────────────────────────┐");
		log.debug("│ selectByBBoxsTest()        │");
		log.debug("└────────────────────────────┘");
		
		assertNotNull(list);
		log.debug("BBox 검색 건수 : {}", list.size());
		list.forEach(list);
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
