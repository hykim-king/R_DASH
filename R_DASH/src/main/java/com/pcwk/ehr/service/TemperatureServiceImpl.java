package com.pcwk.ehr.service;

import java.util.Collections;
import java.util.List;

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
    private static final String SERVICE_KEY = "VJxg5p3Iyzp7FA0pzgtVA7AYRfaM2YSuLU4h8TMQAvQIJMGkIN7qEpL/QoDBEqo1MnsWnxGR+lN/9SsKlSmbZg==";
    
   
    public TemperatureServiceImpl(RestTemplate restTemplate, TemperatureMapper temperatureMapper ) {
        this.restTemplate = restTemplate;
        this.temperatureMapper  = temperatureMapper ;
    }

    public void fetchAndSaveData() {
    	int pageNo = 1;
        int numOfRows = 100;

        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("ServiceKey", SERVICE_KEY)
                .queryParam("bas_yy", "2024")
                .queryParam("type", "json")
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .toUriString();

        System.out.println("최종 요청 URL: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // JSON으로 바꿔야 함
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<PatientsApiResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                PatientsApiResponse.class
        );

        PatientsApiResponse responseBody = response.getBody();

        if (responseBody != null && responseBody.getHeatWaveCasualtiesRegion() != null) {
            for (PatientsApiResponse.HeatWaveCasualtiesRegionItem item : responseBody.getHeatWaveCasualtiesRegion()) {
                List<PatientsApiResponse.Row> rowList = item.getRow(); // row가 있을 때만
                if (rowList != null) {
                    for (PatientsApiResponse.Row row : rowList) {
                        PatientsDTO dto = convertToDTO(row);
                        savePatient(dto);
                    }
                }
            }
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
	public void savePatient(PatientsDTO dto) {
		temperatureMapper.insertPatient(dto);
	}

	@Override
	public List<PatientsDTO> getAllPatients() {
		return temperatureMapper.selectAllPatients();
	}


}

