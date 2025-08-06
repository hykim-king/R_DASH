package com.pcwk.ehr.service;

import java.net.URI;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.pcwk.ehr.Response.PatientsApiResponse;
import com.pcwk.ehr.domain.PatientsDTO;
import com.pcwk.ehr.mapper.TemperatureMapper;

@Service
public class TemperatureServiceImpl implements TemperatureService {

	@Autowired
    private RestTemplate restTemplate;
	private TemperatureMapper temperatureMapper;
	
    private static final String BASE_URL = "http://apis.data.go.kr/1741000/HeatWaveCasualtiesRegion/getHeatWaveCasualtiesRegionList";
    private static final String SERVICE_KEY = "VJxg5p3Iyzp7FA0pzgtVA7AYRfaM2YSuLU4h8TMQAvQIJMGkIN7qEpL%2FQoDBEqo1MnsWnxGR%2BlN%2F9SsKlSmbZg%3D%3D";
    
   
    public TemperatureServiceImpl(RestTemplate restTemplate, TemperatureMapper temperatureMapper ) {
        this.restTemplate = restTemplate;
        this.temperatureMapper  = temperatureMapper ;
    }
    
  
    public String fetchAndSaveData() {
    	try {
            URI uri = new URI(BASE_URL +
                    "?serviceKey=" + SERVICE_KEY +
                    "&type=json" + 
                    "&bas_yy=2022" +
                    "&pageNo=1" +
                    "&numOfRows=100");

            // HttpHeaders 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "text/html");  // 여기서 핵심!

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // RestTemplate로 GET 요청
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "오류 발생: " + e.getMessage();
        }
    }

    private PatientsDTO convertToDTO(PatientsApiResponse.Row row) {
    	String region = row.getRegi();
        int year = Integer.parseInt(row.getBas_yy());
        int total = parseIntSafe(row.getTot());
        int outdoor = parseIntSafe(row.getOtdoor_subtot());
        int indoor = parseIntSafe(row.getIndoor_subtot());

        PatientsDTO patientsDTO = new PatientsDTO(null, region, year, total, outdoor, indoor);
        return patientsDTO;
    }

    private int parseIntSafe(String val) {
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return 0;
        }
    }
    
	@Override
	public void savePatient(PatientsDTO dto) throws SQLException {
		temperatureMapper.insertPatient(dto);
	}

	@Override
	public List<PatientsDTO> getAllPatients() throws SQLException {
		return temperatureMapper.selectAllPatients();
	}


}

