package com.pcwk.ehr.controller;

import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriUtils;

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
            @RequestParam(required = false, defaultValue = "500") Integer limit
    ) {
        boolean hasBBox = (minLat != null && maxLat != null && minLon != null && maxLon != null);
        if (hasBBox && minLat > maxLat) { double t = minLat; minLat = maxLat; maxLat = t; }
        if (hasBBox && minLon > maxLon) { double t = minLon; minLon = maxLon; maxLon = t; }

        String dayStr = (day == null || day.trim().isEmpty()) ? null : day.trim();
        int lim = (limit == null || limit <= 0) ? 500 : limit;

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

    @GetMapping("/dust-avg")
    @ResponseBody
    public Double getAvgPM10() {
        return dustService.getAvgPM10();
    }
    
    @GetMapping("/statsPage")
	public String statsPage(Model model) throws SQLException {
		model.addAttribute("pageType", "dust");

		return "stats/statsMain";
	}
}