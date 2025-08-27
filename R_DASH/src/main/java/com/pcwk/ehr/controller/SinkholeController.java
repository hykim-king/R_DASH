package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.domain.SinkholeDTO;
import com.pcwk.ehr.service.SinkholeService;

/**
 * 싱크홀 API 컨트롤러 - 지도 BBox 내 검색(+ 선택 검색어/기간) - 단건 상세 - 상태별 집계
 */
@Controller
@RequestMapping("/sinkholes")
public class SinkholeController {

	@Autowired
	private SinkholeService sinkholeService;

	 // 간단 헬스체크
    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "ok";
    }


    // 1) 히트맵 포인트 (전체) : optional year/stateNm
    //   → SELECT LAT,LON,1 AS weight
    @GetMapping("/points/all")
    @ResponseBody
    public List<Map<String, Object>> allPoints(
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String stateNm
    ) {
        return sinkholeService.selectAllPoints(year, stateNm);
    }

    // 2) 히트맵 포인트 (현재 지도 BBox, 최대 5000건)
    @GetMapping("/points")
    @ResponseBody
    public List<SinkholeDTO> pointsByBBox(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String stateNm
    ) throws Exception {

        // 파라미터 보정: min > max 로 들어오면 스왑
        double minLa = Math.min(minLat, maxLat);
        double maxLa = Math.max(minLat, maxLat);
        double minLo = Math.min(minLon, maxLon);
        double maxLo = Math.max(minLon, maxLon);

        return sinkholeService.selectPointsByBBox(minLa, maxLa, minLo, maxLo, year, stateNm);
    }

    // 3) 버블(격자) 집계: 현재 지도 BBox + round(격자 정밀도)
    @GetMapping("/bubbles")
    @ResponseBody
    public List<SinkholeDTO> bubblesInView(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon,
            @RequestParam(defaultValue = "4") int round,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String stateNm
    ) throws Exception {

        double minLa = Math.min(minLat, maxLat);
        double maxLa = Math.max(minLat, maxLat);
        double minLo = Math.min(minLon, maxLon);
        double maxLo = Math.max(minLon, maxLon);

        return sinkholeService.selectBubblesByBBox(minLa, maxLa, minLo, maxLo, round, year, stateNm);
    }

    // 4) 버블(행정구역) 집계: 시·도 선택(optional), 연도/상태 필터
    @GetMapping("/bubbles/region")
    @ResponseBody
    public List<SinkholeDTO> bubblesByRegion(
            @RequestParam(required = false) String sido,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String stateNm
    ) throws Exception {
        return sinkholeService.selectBubbleStats(sido, year, stateNm);
    }


	@GetMapping("/year")
	@ResponseBody
	public List<Map<String, Object>> yearlyCounts() {
		return sinkholeService.getYearlyCounts();
	}

	@GetMapping("/sido")
	@ResponseBody
	public List<Map<String, Object>> signguCounts() {
		return sinkholeService.getSignguCounts();
	}

	@GetMapping("/month")
	@ResponseBody
	public List<Map<String, Object>> monthlyCounts() {
		return sinkholeService.getMonthlyCounts();
	}

	@GetMapping("/state")
	@ResponseBody
	public List<Map<String, Object>> stateDistribution() {
		return sinkholeService.countByState();
	}

	@GetMapping("/damage")
	@ResponseBody
	public List<Map<String, Object>> yearlyDamageStats() {
		return sinkholeService.getYearlyDamageStats();
	}

	@GetMapping("/statsPage")
	public String statsPage(Model model) throws SQLException {
		model.addAttribute("pageType", "sinkhole");

		return "stats/statsMain";
	}
}
