package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
	
    private static final String BASE_URL = "https://apis.data.go.kr/1741000/HeatWaveCasualtiesRegion";
    private static final String SERVICE_KEY = "VJxg5p3Iyzp7FA0pzgtVA7AYRfaM2YSuLU4h8TMQAvQIJMGkIN7qEpL/QoDBEqo1MnsWnxGR+lN/9SsKlSmbZg==\r\n";
    
    

    public TemperatureServiceImpl(RestTemplate restTemplate, TemperatureMapper temperatureMapper ) {
        this.restTemplate = restTemplate;
        this.temperatureMapper  = temperatureMapper ;
    }

    public void fetchAndSaveData() {
    	int pageNo = 1;
    	int numOfRows = 100;
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("type", "json")
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .toUriString();

        PatientsApiResponse response = restTemplate.getForObject(url, PatientsApiResponse.class);

        if (response != null && response.getRow() != null) {
            for (PatientsApiResponse.Row row : response.getRow()) {
                PatientsDTO dto = convertToDTO(row);
                savePatient(dto);
            }
        }
        
        System.out.println("6시간마다 데이터 갱신");
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
