package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.pcwk.ehr.Response.PatientsApiResponse;
import com.pcwk.ehr.domain.PatientsDTO;
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
	void testFetchAndSaveData() {
	    PatientsApiResponse.Row row = new PatientsApiResponse.Row();
	    row.setRegi("서울");
	    row.setBas_yy("2024");
	    row.setTot("100");
	    row.setOtdoor_subtot("60");
	    row.setIndoor_subtot("40");
	
	    PatientsApiResponse response = new PatientsApiResponse();
	    response.setRow(Arrays.asList(row));
	    // RestTemplate가 특정 URL 호출 시 위 응답 반환하도록 설정
	    when(restTemplate.getForObject(anyString(), eq(PatientsApiResponse.class)))
	            .thenReturn(response);
	
	    temperatureService.fetchAndSaveData();
	    
	    ArgumentCaptor<PatientsDTO> captor = ArgumentCaptor.forClass(PatientsDTO.class);
	    verify(temperatureMapper, times(1)).insertPatient(captor.capture());

	    PatientsDTO saved = captor.getValue();
	    log.info("insertPatient()에 전달된 DTO: {}", saved);
	}
	
	@Disabled
	@Test
	void beans() {
		fail("Not yet implemented");
	}

}
