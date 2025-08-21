package com.pcwk.ehr.controller;

import java.nio.charset.StandardCharsets;
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

    @Autowired
    public DustController(DustService svc) {
        this.dustService = svc;
    }

    // /dust → /map?layer=dust&airType=ALL (혹은 전달된 값)로 리다이렉트
    @GetMapping({"", "/"})
    public String dustEntry(@RequestParam(required = false, defaultValue = "ALL") String airType) {
        String type = airType != null ? airType.trim() : "ALL";
        if (type.isEmpty()) type = "ALL";
        String enc  = UriUtils.encode(type, StandardCharsets.UTF_8);
        return "redirect:/map?layer=dust&airType=" + enc;
    }

    // 데이터 API: 단일 타입 최신 (BBox 있으면 BBox, 없으면 전국)
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

        String type   = (airType == null || airType.trim().isEmpty()) ? null : airType.trim();
        String dayStr = (day == null || day.trim().isEmpty()) ? null : day.trim();
        int lim       = (limit == null || limit <= 0) ? 500 : limit;

        return hasBBox
                ? dustService.getLatestByTypeBBox(type, dayStr, minLat, maxLat, minLon, maxLon, lim)
                : dustService.getLatestByTypeAll(type, dayStr, lim);
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