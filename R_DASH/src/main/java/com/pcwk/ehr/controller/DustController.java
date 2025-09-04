package com.pcwk.ehr.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.domain.DustDTO;
import com.pcwk.ehr.service.DustService;

/**
 * 프런트에서 airType 하나만 선택해서 호출
 * - BBox 파라미터가 모두 오면 BBox로
 * - 아니면 전국 조회
 */
@Controller
@RequestMapping("/dust")
public class DustController {
    private final DustService dustService;
    private final String KAKAO_REST_API_KEY = "27dac2cfbdc8ac4034782eae59ebfeee";
    
    public DustController(DustService svc){ this.dustService = svc; }

//    // 페이지: /ehr/dust  (map.jsp 불필요)
//    @GetMapping("/ehr/dust")
//    public String dustPage(
//            @RequestParam(defaultValue = "ALL") String airType,
//            org.springframework.ui.Model model
//    ) {
//        model.addAttribute("airType", airType);
//        return "ehr/dust"; // → /WEB-INF/views/ehr/dust.jsp
//    }

    // 데이터 API: /dust/latest
    @GetMapping("/latest")
    @ResponseBody
    public List<DustDTO> latest(
            @RequestParam("airType") String airType,
            @RequestParam(required = false) String day,
            @RequestParam(required = false) Double minLat,
            @RequestParam(required = false) Double maxLat,
            @RequestParam(required = false) Double minLon,
            @RequestParam(required = false) Double maxLon,
            @RequestParam(required = false, defaultValue = "600") Integer limit
    ) {
        boolean hasBBox = (minLat != null && maxLat != null && minLon != null && maxLon != null);
        if (hasBBox && minLat > maxLat) { double t = minLat; minLat = maxLat; maxLat = t; }
        if (hasBBox && minLon > maxLon) { double t = minLon; minLon = maxLon; maxLon = t; }

        String dayStr = (day == null || day.trim().isEmpty()) ? null : day.trim();
        int lim = (limit == null || limit <= 0) ? 600 : limit;

        String raw = airType == null ? "" : airType.trim();
        String key = raw.replaceAll("\\s+", "").toLowerCase();

        String typeCanon;
        if ("all".equals(key) || "전체".equals(raw)) typeCanon = "ALL";
        else if ("교외대기".equals(key))           typeCanon = "교외대기";
        else if ("도로변대기".equals(key))          typeCanon = "도로변 대기"; // DB 표준에 맞춰 변경
        else if ("도시대기".equals(key))           typeCanon = "도시대기";
        else                                       typeCanon = raw;

        if ("ALL".equals(typeCanon)) {
            String road = "도로변 대기";
            String[] TYPES = { "교외대기", road, "도시대기" };
            int per = Math.max(1, lim / TYPES.length);
            List<DustDTO> out = new ArrayList<>();
            for (String t : TYPES) {
                List<DustDTO> part = hasBBox
                        ? dustService.getLatestByTypeBBox(t, dayStr, minLat, maxLat, minLon, maxLon, per)
                        : dustService.getLatestByTypeAll(t,  dayStr, per);
                if (part != null && !part.isEmpty()) out.addAll(part);
            }
            return out;
        }

        String type = typeCanon.isEmpty() ? null : typeCanon;
        return hasBBox
                ? dustService.getLatestByTypeBBox(type, dayStr, minLat, maxLat, minLon, maxLon, lim)
                : dustService.getLatestByTypeAll(type,  dayStr, lim);
                
                
    }

    
    
    
    
    
    
    @GetMapping("/dust-top5")
    @ResponseBody
    public List<Map<String, Object>> getTop5PM10() {
        return dustService.getTop5PM10();
    }

    @GetMapping("/dust-bottom5")
    @ResponseBody
    public List<Map<String, Object>> getBottom5PM10() {
        return dustService.getBottom5PM10();
    }

    
    @GetMapping("/geocode")
    @ResponseBody
    public Map<String, Object> geocode(@RequestParam("address") String address) {
        Map<String, Object> result = new HashMap<>();
        HttpURLConnection con = null;
        
        try {
            String urlStr = "https://dapi.kakao.com/v2/local/search/address.json?query=" 
                            + URLEncoder.encode(address, "UTF-8");
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
                JsonNode doc = documents.get(0);
                double lat = doc.path("y").asDouble();
                double lon = doc.path("x").asDouble();
                result.put("lat", lat);
                result.put("lon", lon);
            } else {
                result.put("lat", null);
                result.put("lon", null);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result.put("lat", null);
            result.put("lon", null);
        }
        return result;
    }
    
    @GetMapping("/dust-avg")
    @ResponseBody
    public Map<String, Object> getAvgPM10(
    		@RequestParam(value="userLat", required=false) String userLatStr,
            @RequestParam(value="userLon", required=false) String userLonStr) {

    	Map<String, Object> result = new HashMap<>();
        
    	Double userLat = (userLatStr != null && !userLatStr.isEmpty()) ? Double.valueOf(userLatStr) : null;
    	Double userLon = (userLonStr != null && !userLonStr.isEmpty()) ? Double.valueOf(userLonStr) : null;
    	    
        if (userLat == null || userLon == null) {
            result.put("region", "전국");
            result.put("value", dustService.getAvgPM10(null));
        } else {
            DustDTO nearestDust = dustService.findNearestDust(userLat, userLon);
            Double avg = dustService.getAvgPM10(nearestDust.getStnNm());
            result.put("region", nearestDust.getStnNm());
            result.put("value", avg);
        }
        
        return result;
	}

    @GetMapping("/statsPage")
	public String statsPage(Model model) throws SQLException {
		model.addAttribute("pageType", "dust");

		return "stats/statsMain";
	}
}