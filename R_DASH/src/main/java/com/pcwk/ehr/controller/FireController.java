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

import com.pcwk.ehr.mapper.FireMapper;
import com.pcwk.ehr.service.FireService;

@Controller
@RequestMapping("/fire")
public class FireController {
	
	@Autowired
    private FireMapper fireMapper;

	@Autowired
	private FireService fireService;
	
	
	@GetMapping("/fire-safe.do")
	@ResponseBody
    public List<Map<String, Object>> getFireSafe() {
        return fireService.getSelectFireSafe();
    }
	
	
	@GetMapping("/fire-stations.do")
	@ResponseBody
	public List<Map<String, Object>> getFireStation() throws SQLException {
	    return fireService.getfirestationCounts();
	}
	
	@GetMapping("/statsPage")
	public String statsPage(Model model) throws SQLException {
		model.addAttribute("pageType", "fire"); // 기본값: weather

		return "stats/statsMain";
	}
}
