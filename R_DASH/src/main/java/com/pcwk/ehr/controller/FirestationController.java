package com.pcwk.ehr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.pcwk.ehr.domain.FirestationDTO;
import com.pcwk.ehr.service.FirestationService;

@Controller
@RequestMapping("/firestations")
public class FirestationController {
	
	Logger log = LogManager.getLogger(getClass());
	
	
    @Autowired
    private FirestationService service;

    /** BBox 조회: /firestations/bbox?minLat&maxLat&minLon&maxLon&limit&fireTp&fireTpLabel */

    @GetMapping(value = "/bbox", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FirestationDTO> bbox(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon,
            @RequestParam(defaultValue = "1000") int limit,
            @RequestParam(required = false) String fireTp,
            @RequestParam(required = false) String fireTpLabel
    ) {
        return service.selectByBBox(minLat, maxLat, minLon, maxLon, limit, fireTp, fireTpLabel);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<FirestationDTO> search(
            @RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String fireTp,
            @RequestParam(required = false) String fireTpLabel
    ) {
        return service.search(q, limit, offset, fireTp, fireTpLabel);
    }

    /** 단건 상세 */
    @GetMapping(value="/{stationNo}", produces="application/json; charset=UTF-8")
    @ResponseBody
    public FirestationDTO one(@PathVariable int stationNo) {
        return service.selectOne(stationNo);
    }

    /** 자동완성: 지역 (fireTp도 선택적으로 전달) */
    @GetMapping(value="/auto/area", produces="application/json; charset=UTF-8")
    @ResponseBody
    public List<String> autoArea(@RequestParam String prefix,
                                 @RequestParam(required=false, defaultValue="10") Integer limit,
                                 @RequestParam(required=false, defaultValue="ALL") String fireTp) {
        return service.autocompleteArea(prefix, limit, fireTp);
    }

    /** 자동완성: 소방서명 (fireTp도 선택적으로 전달) */
    @GetMapping(value="/auto/station", produces="application/json; charset=UTF-8")
    @ResponseBody
    public List<String> autoStation(@RequestParam String prefix,
                                    @RequestParam(required=false) String area,
                                    @RequestParam(required=false, defaultValue="10") Integer limit,
                                    @RequestParam(required=false, defaultValue="ALL") String fireTp) {
        return service.autocompleteStation(prefix, area, limit, fireTp);
    }
}