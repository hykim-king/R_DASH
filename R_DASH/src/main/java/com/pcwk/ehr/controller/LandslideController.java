package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    
    /**
     * 버블 집계
     * - level=sgg(기본): 시군구 단위
     * - level=sido     : 시/도 단위(줌 아웃)
     */
    @GetMapping("/bubbles")
    @ResponseBody
    public List<Map<String,Object>> bubbles(
        @RequestParam double minLat, @RequestParam double maxLat,
        @RequestParam double minLon, @RequestParam double maxLon,
        @RequestParam(defaultValue = "sgg") String level,
        @RequestParam(required = false) String q
    ){
        log.debug("GET /landslide/bubbles level={}, bbox=({},{} ~ {},{}), q={}",
                level, minLat, minLon, maxLat, maxLon, q);

        final List<Map<String,Object>> result =
            "sido".equalsIgnoreCase(level)
                ? mapper.countBySidoInBBox(minLat, maxLat, minLon, maxLon, q)
                : mapper.countByRegionInBBox(minLat, maxLat, minLon, maxLon, q);

        log.debug("bubbles result size={}", (result == null ? 0 : result.size()));
        return result;
    }

    /** BBox 내 포인트(단건) 목록 */
    @GetMapping("/points")
    @ResponseBody
    public List<LandslideDTO> points(
        @RequestParam double minLat, @RequestParam double maxLat,
        @RequestParam double minLon, @RequestParam double maxLon,
        @RequestParam(required = false) String q
    ){
        log.debug("GET /landslide/points bbox=({},{} ~ {},{}), q={}",
                minLat, minLon, maxLat, maxLon, q);

        final List<LandslideDTO> result =
            mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q);

        log.debug("points result size={}", (result == null ? 0 : result.size()));
        return result;
    }

    /** 단건 상세 */
    @GetMapping("/{id}")
    @ResponseBody
    public LandslideDTO detail(@PathVariable("id") Long id){
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
