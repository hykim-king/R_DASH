package com.pcwk.ehr.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.domain.DustDTO;
import com.pcwk.ehr.mapper.DustMapper;

@Service
public class DustServiceImpl implements DustService {

	private final String KAKAO_REST_API_KEY = "27dac2cfbdc8ac4034782eae59ebfeee";
	
    @Autowired
    private DustMapper dustMapper;
    
    
    @Autowired
    public DustServiceImpl(DustMapper dustMapper) {
        this.dustMapper = dustMapper;
    }

    @Override
    public List<DustDTO> getLatestByTypeBBox(String airType, String day,
                                             Double minLat, Double maxLat,
                                             Double minLon, Double maxLon,
                                             Integer limit) {
        return dustMapper.selectLatestByTypeBBox(airType, day, minLat, maxLat, minLon, maxLon, limit);
    }

    @Override
    public List<DustDTO> getLatestByTypeAll(String airType, String day, Integer limit) {
        return dustMapper.selectLatestByTypeAll(airType, day, limit);
    }
    
    
    public Map<String, String> reverseGeocode(double lat, double lon) {
        Map<String, String> result = new HashMap<>();
        HttpURLConnection con = null;

        try {
            String urlStr = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=" 
                            + lon + "&y=" + lat;
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "KakaoAK " + KAKAO_REST_API_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // JSON 파싱
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.toString());
            JsonNode documents = root.path("documents");

            if (documents.size() > 0) {
                JsonNode address = documents.get(0).path("address");
                String sido = address.path("region_1depth_name").asText();
                String sigungu = address.path("region_2depth_name").asText();
                String fullAddress = sido + " " + sigungu;
                
                result.put("sido", sido);
                result.put("sigungu", sigungu);
                result.put("fullAddress", fullAddress);
            } else {
                result.put("sido", null);
                result.put("sigungu", null);
                result.put("fullAddress", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("sido", null);
            result.put("sigungu", null);
            result.put("fullAddress", null);
        }
        return result;
    }
    
 // 공통 처리: fullAddress 변환 + RNK별 합치기
    private List<Map<String,Object>> processDustList(List<Map<String,Object>> list) {
        Map<Integer, Set<String>> rnkMap = new LinkedHashMap<>();

        for (Map<String,Object> row : list) {
            int rank = ((Number)row.get("RNK")).intValue();
            double lat = ((Number) row.get("LAT")).doubleValue();
            double lon = ((Number) row.get("LON")).doubleValue();
            String stnNm = (String) row.get("STN_NM");
            
            Map<String,String> addr = reverseGeocode(lat, lon);
            String fullAddress = addr.get("fullAddress")+ " " + stnNm;

            rnkMap.computeIfAbsent(rank, k -> new LinkedHashSet<>()).add(fullAddress);
        }

        List<Map<String,Object>> result = new ArrayList<>();
        for (Map.Entry<Integer,Set<String>> entry : rnkMap.entrySet()) {
            Map<String,Object> map = new HashMap<>();
            map.put("RNK", entry.getKey());
            map.put("STN_LIST", String.join("<br>", entry.getValue()));
            map.put("PM10", list.stream()
                                .filter(r -> ((Number)r.get("RNK")).intValue() == entry.getKey())
                                .findFirst()
                                .get()
                                .get("PM10"));
            result.add(map);
        }
        return result;
    }
    
    
    @Override
    public List<Map<String, Object>> getTop5PM10() {
        List<Map<String,Object>> list = dustMapper.selectTop5PM10();
        return processDustList(list); 
    }

    @Override
    public List<Map<String, Object>> getBottom5PM10() {
        List<Map<String,Object>> list = dustMapper.selectBottom5PM10();
        return processDustList(list);
    }

    @Override
    public Double getAvgPM10(String region) {
        return dustMapper.selectAvgPM10(region);
    }

    @Override
    public DustDTO findNearestDust(double userLat, double userLon) {
        return dustMapper.findNearestDust(userLat, userLon);
    }
}