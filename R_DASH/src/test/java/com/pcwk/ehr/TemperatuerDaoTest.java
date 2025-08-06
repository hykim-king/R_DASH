package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.pcwk.ehr.mapper.TemperatureMapper;
import com.pcwk.ehr.service.TemperatureServiceImpl;

@ExtendWith(MockitoExtension.class)
class TemperatuerDaoTest {
	
	Logger log = LogManager.getLogger(getClass());

	@Mock
	private RestTemplate restTemplate;
	
	@Mock
	private TemperatureMapper temperatureMapper;
	
	@InjectMocks
	private TemperatureServiceImpl temperatureService;
	
	@Autowired
	MockMvc mockMvc;
	
	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");
		
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}
	
	
	@Disabled
	@Test
	void beans() {
		fail("Not yet implemented");
	}

}
