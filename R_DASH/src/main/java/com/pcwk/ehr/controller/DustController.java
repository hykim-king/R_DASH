package com.pcwk.ehr.controller;

import java.sql.SQLException;
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

    @Autowired
    public DustController(DustService dustService) {
        this.dustService = dustService;
    }

    @GetMapping(value = "/latest", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<DustDTO> latest(
            @RequestParam String airType,                   // ex) "교외대기", "도로변대기", "도시대기"
            @RequestParam(required = false) String day,     // ex) "2025-08-17" (옵션)
            @RequestParam(required = false) Double minLat,
            @RequestParam(required = false) Double maxLat,
            @RequestParam(required = false) Double minLon,
            @RequestParam(required = false) Double maxLon,
            @RequestParam(required = false, defaultValue = "500") Integer limit
    ) {
        // 1) 파라미터 정리
        final String type = airType == null ? null : airType.trim();   // ORG 비교용
        final String dayStr = (day == null || day.trim().isEmpty()) ? null : day.trim();
        final int lim = (limit == null || limit <= 0) ? 500 : limit;

        // 2) BBox 유효성: 네 값 모두 있을 때만 사용
        boolean hasBBox = (minLat != null && maxLat != null && minLon != null && maxLon != null);
        // min/max 뒤바뀐 경우 보정
        if (hasBBox && minLat > maxLat) {
            double t = minLat; minLat = maxLat; maxLat = t;
        }
        if (hasBBox && minLon > maxLon) {
            double t = minLon; minLon = maxLon; maxLon = t;
        }

        // 3) 분기
        if (hasBBox) {
            return dustService.getLatestByTypeBBox(type, dayStr, minLat, maxLat, minLon, maxLon, lim);
        } else {
            return dustService.getLatestByTypeAll(type, dayStr, lim);
        }
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