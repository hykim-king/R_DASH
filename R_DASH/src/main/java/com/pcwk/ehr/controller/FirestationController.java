package com.pcwk.ehr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.pcwk.ehr.domain.FirestationDTO;
import com.pcwk.ehr.service.FirestationService;

@Controller
@RequestMapping("/api/firestations")
public class FirestationController {
	
	Logger log = LogManager.getLogger(getClass());
	
	
    @Autowired
    private FirestationService service;

    /** 지도 BBox 조회 */
    @GetMapping(value="/bbox", produces="application/json; charset=UTF-8")
    @ResponseBody
    public List<FirestationDTO> bbox(
            @RequestParam double minLat, @RequestParam double maxLat,
            @RequestParam double minLon, @RequestParam double maxLon,
            @RequestParam(required=false) String q,
            @RequestParam(required=false) Integer limit) {
        return service.selectByBBox(minLat, maxLat, minLon, maxLon, q, limit);
    }

    /** 검색 (목록 + 페이징 + 정렬 + BBox) */
    @GetMapping(value="/search", produces="application/json; charset=UTF-8")
    @ResponseBody
    public Map<String, Object> search(
            @RequestParam(required=false) String q,
            @RequestParam(required=false) String area,
            @RequestParam(required=false) Double minLat,
            @RequestParam(required=false) Double maxLat,
            @RequestParam(required=false) Double minLon,
            @RequestParam(required=false) Double maxLon,
            @RequestParam(required=false, defaultValue="name") String orderBy,
            @RequestParam(required=false, defaultValue="20") Integer limit,
            @RequestParam(required=false, defaultValue="0") Integer offset) {

        List<FirestationDTO> rows = service.search(
                q, area, minLat, maxLat, minLon, maxLon, orderBy, limit, offset);
        int total = service.countSearch(q, area, minLat, maxLat, minLon, maxLon);

        Map<String, Object> res = new HashMap<>();
        res.put("rows", rows);
        res.put("total", total);
        res.put("limit", limit);
        res.put("offset", offset);
        return res;
    }

    /** 단건 상세 */
    @GetMapping(value="/{stationNo}", produces="application/json; charset=UTF-8")
    @ResponseBody
    public FirestationDTO one(@PathVariable int stationNo) {
        return service.selectOne(stationNo);
    }

    /** 자동완성: 지역 */
    @GetMapping(value="/auto/area", produces="application/json; charset=UTF-8")
    @ResponseBody
    public List<String> autoArea(@RequestParam String prefix,
                                 @RequestParam(required=false, defaultValue="10") Integer limit) {
        return service.autocompleteArea(prefix, limit);
    }

    /** 자동완성: 소방서명 */
    @GetMapping(value="/auto/station", produces="application/json; charset=UTF-8")
    @ResponseBody
    public List<String> autoStation(@RequestParam String prefix,
                                    @RequestParam(required=false) String area,
                                    @RequestParam(required=false, defaultValue="10") Integer limit) {
        return service.autocompleteStation(prefix, area, limit);
    }
}
