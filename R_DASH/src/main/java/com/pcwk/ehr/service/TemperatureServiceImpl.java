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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.Response.PatientsApiResponse;
import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.domain.PatientsDTO;
import com.pcwk.ehr.mapper.TemperatureMapper;

@Service
public class TemperatureServiceImpl implements TemperatureService {

	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
	private TemperatureMapper temperatureMapper;
	
    private static final String BASE_URL = "http://apis.data.go.kr/1741000/HeatWaveCasualtiesRegion/getHeatWaveCasualtiesRegionList";
    private static final String SERVICE_KEY = "VJxg5p3Iyzp7FA0pzgtVA7AYRfaM2YSuLU4h8TMQAvQIJMGkIN7qEpL%2FQoDBEqo1MnsWnxGR%2BlN%2F9SsKlSmbZg%3D%3D";
    
   
    public TemperatureServiceImpl(RestTemplate restTemplate, TemperatureMapper temperatureMapper ) {
        this.restTemplate = restTemplate;
        this.temperatureMapper  = temperatureMapper ;
    }

    private PatientsDTO patientsConvertToDTO(PatientsApiResponse.Row row) {
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
	public void insertPatient() throws SQLException {
		try {
	        // 1. API 호출
	        URI uri = new URI(BASE_URL +
	                "?serviceKey=" + SERVICE_KEY +
	                "&type=json" +
	                "&bas_yy=" +
	                "&pageNo=1" +
	                "&numOfRows=500");

	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Accept", "text/html");

	        HttpEntity<String> entity = new HttpEntity<>(headers);
	        RestTemplate restTemplate = new RestTemplate();

	        ResponseEntity<String> response = restTemplate.exchange(
	                uri,
	                HttpMethod.POST,
	                entity,
	                String.class
	        );

	        // 2. JSON → 객체 변환
	        ObjectMapper objectMapper = new ObjectMapper();
	        PatientsApiResponse apiResponse =
	                objectMapper.readValue(response.getBody(), PatientsApiResponse.class);

	        // 3. HeatWaveCasualtiesRegion → Row 변환 → DTO 변환 → DB 저장
	        List<Map<String, Object>> regionList = apiResponse.getHeatWaveCasualtiesRegion();

	        for (Map<String, Object> regionMap : regionList) {
	            @SuppressWarnings("unchecked")
	            List<Map<String, Object>> rowList = (List<Map<String, Object>>) regionMap.get("row");

	            if (rowList == null) {
	                System.out.println("rowList is null for regionMap: " + regionMap);
	                continue;  // null일 경우 다음 regionMap으로 넘어감
	            }
	            
	            for (Map<String, Object> rowMap : rowList) {
	                PatientsApiResponse.Row row = objectMapper.convertValue(rowMap, PatientsApiResponse.Row.class);

	                // Row → DTO
	                PatientsDTO dto1 = patientsConvertToDTO(row);

	                // 매퍼로 DB 저장
	                temperatureMapper.insertPatient(dto1);
	            }
	        }

	        System.out.println("데이터 저장 완료!");

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("데이터 저장 중 오류 발생: " + e.getMessage());
	    }
	}

	@Override
	public List<PatientsDTO> getAllPatients() throws SQLException {
		return temperatureMapper.selectAllPatients();
	}


	@Override
	public void saveNowcast(NowcastDTO dto) throws SQLException {
		temperatureMapper.insertNowcast(dto);
		
	}

	@Override
	public void savePatient(PatientsDTO dto) throws SQLException {
		temperatureMapper.insertPatient(dto);
	}


}

