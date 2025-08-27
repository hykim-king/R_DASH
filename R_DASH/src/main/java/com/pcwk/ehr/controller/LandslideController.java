package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pcwk.ehr.domain.LandslideDTO;
import com.pcwk.ehr.mapper.LandslideMapper;
import com.pcwk.ehr.service.LandslideService;

@Controller
@RequestMapping("/landslide")
public class LandslideController {

	private final Logger log = LogManager.getLogger(getClass());

	@Autowired
	LandslideMapper mapper;

	@Autowired
	LandslideService service;

	@GetMapping(value={"/byRegionInBBox.do","/byRegionInBBox"}, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object byRegionInBBox(
	    @RequestParam double minLat, @RequestParam double maxLat,
	    @RequestParam double minLon, @RequestParam double maxLon,
	    @RequestParam(required=false) String q,
	    @RequestParam(required=false) String year
	){
	  try {
	    String qNorm = (q==null || q.trim().isEmpty()) ? null : q.trim();
	    Integer yearNorm = (year==null || year.trim().isEmpty()) ? null : Integer.valueOf(year.trim());
	    return service.countByRegionInBBox(minLat,maxLat,minLon,maxLon,qNorm,yearNorm);
	  } catch (Exception e) {
	    Map<String,Object> err = new LinkedHashMap<>();
	    err.put("error", e.getClass().getSimpleName());
	    err.put("message", e.getMessage());
	    return err;
	  }
	}

	@GetMapping(value={"/points.do","/points"}, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<LandslideDTO> points(
	    @RequestParam double minLat, @RequestParam double maxLat,
	    @RequestParam double minLon, @RequestParam double maxLon,
	    @RequestParam(required=false) String q,
	    @RequestParam(required=false) String year
	){
	  String qNorm = (q == null || q.trim().isEmpty()) ? null : q.trim();
	  Integer yearNorm = (year == null || year.trim().isEmpty()) ? null : Integer.valueOf(year.trim());
	  return service.selectByBBox(minLat,maxLat,minLon,maxLon,qNorm,yearNorm);
	}

	@GetMapping(value={"/countBySidoInBBox.do","/countBySidoInBBox"}, produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String,Object>> countBySidoInBBox(
	    @RequestParam double minLat, @RequestParam double maxLat,
	    @RequestParam double minLon, @RequestParam double maxLon,
	    @RequestParam(required=false) String q,
	    @RequestParam(required=false) String year
	){
	  String qNorm = (q == null || q.trim().isEmpty()) ? null : q.trim();
	  Integer yearNorm = (year == null || year.trim().isEmpty()) ? null : Integer.valueOf(year.trim());
	  return service.countBySidoInBBox(minLat,maxLat,minLon,maxLon,qNorm,yearNorm);
	}
	  
	  
	  
	/** 단건 상세 (PK 기반) */
	@GetMapping("/{id}")
	@ResponseBody
	public LandslideDTO detail(@PathVariable("id") Long id) {
		log.debug("GET /landslide/{} (detail)", id);
		final LandslideDTO dto = mapper.findById(id);
		log.debug("detail found? {}", (dto != null));
		return dto;
	}
	
	
	
	
	
	
	
	

	@GetMapping("/year")
	@ResponseBody
	public List<Map<String, Object>> getYear() {
		return service.getByYear();
	}

	@GetMapping("/region")
	@ResponseBody
	public List<Map<String, Object>> getRegion() {
		return service.getByRegion();
	}

	@GetMapping("/month")
	@ResponseBody
	public List<Map<String, Object>> getMonth() {
		return service.getByMonth();
	}

	@GetMapping("/status")
	@ResponseBody
	public List<Map<String, Object>> getStatus() {
		return service.getByStatus();
	}

	@GetMapping("/statsPage")
	public String statsPage(Model model) throws SQLException {
		model.addAttribute("pageType", "landslide");

		return "stats/statsMain";
	}
}
