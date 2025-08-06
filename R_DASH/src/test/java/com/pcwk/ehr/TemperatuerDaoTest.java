package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.pcwk.ehr.domain.PatientsDTO;
import com.pcwk.ehr.mapper.TemperatureMapper;
import com.pcwk.ehr.service.TemperatureServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml"
		,"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml"
})
class TemperatuerDaoTest {
	
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	private TemperatureMapper mapper;
	
	@Autowired
	private TemperatureServiceImpl serviceImpl;
	
	PatientsDTO patientsDTO;

	
	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
		patientsDTO = new PatientsDTO(0, "서울", 2025, 330, 300, 30);
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}
	
	@Test
	void doSelectAll() throws SQLException {
		
	}
	
	@Disabled
	@Test
	void doSelectOne() throws SQLException {
		// 1.삭제 후 등록
		doSave();
		
		// 2.한건 조회
		PatientsDTO outVO = mapper.selectSidoPatients("서울");
		assertNotNull(outVO);
		log.debug("outVO:{}",outVO);
	}
	
	@Disabled
	@Test
	void doSave() throws SQLException {
		// 1.전체삭제
		mapper.deleteAll();
		log.debug("Count:{}",mapper.getCount());
		assertEquals(0, mapper.getCount());
		// 2.한건 등록
		int flag = mapper.insertPatient(patientsDTO);
		assertEquals(1, mapper.getCount());
		log.debug("flag:{}",flag);
	}
	
	@Disabled
	@Test
	void beans() {
		assertNotNull(mapper);
		assertNotNull(serviceImpl);
		
		log.debug("temperatureMapper:{}",mapper);
		log.debug("temperatureServiceImpl:{}",serviceImpl);
	}

}
