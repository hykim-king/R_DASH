package com.pcwk.ehr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcwk.ehr.domain.ShelterDTO;
import com.pcwk.ehr.service.ShelterService;

/** 대피소 REST 컨트롤러 */
@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

	
	@Autowired
    ShelterService service;

    @Autowired
    public ShelterController(ShelterService service) {
        this.service = service;
    }

    /** 지도 BBox 조회 */
    @GetMapping("/bbox")
    public List<ShelterDTO> getByBBox(
            @RequestParam double minLat,
            @RequestParam double maxLat,
            @RequestParam double minLon,
            @RequestParam double maxLon,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer limit
    ) throws Exception {
        return service.selectByBBox(minLat, maxLat, minLon, maxLon, q, limit);
    }

    /** 단건 상세 */
    @GetMapping("/{shelterNo}")
    public ShelterDTO getOne(@PathVariable Integer shelterNo) throws Exception {
        return service.selectOne(shelterNo);
    }

    /** 단순 리스트 */
    @GetMapping
    public List<ShelterDTO> list(@RequestParam(required = false) Integer limit) throws Exception {
        return service.selectList(limit);
    }

    /** 자동완성 - 주소 */
    @GetMapping("/suggest/adress")
    public List<String> suggestAdress(@RequestParam String q) throws Exception {
        return service.suggestAdress(q);
    }

    /** 자동완성 - 시설명 */
    @GetMapping("/suggest/name")
    public List<String> suggestName(@RequestParam String q) throws Exception {
        return service.suggestReareNm(q);
    }
}