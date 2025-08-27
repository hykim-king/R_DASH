package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public List<SinkholeDTO> all() {
        return sinkholeService.selectAll();
    }

    @GetMapping("/bbox")
    public List<SinkholeDTO> byBBox(@RequestParam double minLat, @RequestParam double maxLat,
                                    @RequestParam double minLon, @RequestParam double maxLon) {
        return sinkholeService.selectByBBox(minLat, maxLat, minLon, maxLon);
    }

    @GetMapping("/{id}")
    public SinkholeDTO one(@PathVariable int id) {
        return sinkholeService.findById(id);
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


