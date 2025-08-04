package com.pcwk.ehr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class TemperatureServiceImpl implements TemperatureService {

	@Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "https://apis.data.go.kr/1741000/HeatWaveCasualtiesRegion";
    private static final String SERVICE_KEY = "VJxg5p3Iyzp7FA0pzgtVA7AYRfaM2YSuLU4h8TMQAvQIJMGkIN7qEpL/QoDBEqo1MnsWnxGR+lN/9SsKlSmbZg==\r\n";

    //XML을 JSON으로 변환시켜서 가져올 예정
    @Override
    public String getPatients(int pageNo, int numOfRows) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("serviceKey", SERVICE_KEY)
                .queryParam("type", "xml")
                .queryParam("year", "")
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .build()
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }


}
