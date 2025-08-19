package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.service.FireDisasterService;
import com.pcwk.ehr.service.FireExitService;
import com.pcwk.ehr.service.FireSafeService;
import com.pcwk.ehr.service.FirestationService;

@Controller
@RequestMapping("/fire")
public class FireController {
	
	@Autowired
	private FirestationService fireStationService;
	
	@Autowired
	private FireSafeService fireSafeService;
	
	@Autowired
	private FireDisasterService fireDiasterService;
	
	@Autowired
	private FireExitService fireExitService;

	@GetMapping("/fire-ext")
	@ResponseBody
	public List<Map<String,Object>> getSubwayExtCount() {
	    return fireExitService.getSubwayExtCount();
	}

	
	@GetMapping("/fire-yearly")
	@ResponseBody
    public List<Map<String, Object>> getYearlyFire() {
        return fireDiasterService.getYearlyFireCount();
    }

    @GetMapping("/fire-damage")
    @ResponseBody
    public List<Map<String, Object>> getFireDamage() {
        return fireDiasterService.getFireTypeDamage();
    }
    
	@GetMapping("/fire-safe.do")
	@ResponseBody
    public List<Map<String, Object>> getFireSafe() {
        return fireSafeService.getSelectFireSafe();
    }
	
	
	@GetMapping("/fire-stations.do")
	@ResponseBody
	public List<Map<String, Object>> getFireStation() throws SQLException {
	    return fireStationService.getfirestationCounts();
	}
	
	@GetMapping("/statsPage")
	public String statsPage(Model model) throws SQLException {
		model.addAttribute("pageType", "fire"); // 기본값: weather

		return "stats/statsMain";
	}
}
