package com.pcwk.ehr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcwk.ehr.domain.SinkholeDTO;
import com.pcwk.ehr.service.SinkholeService;

/**
 * 싱크홀 API 컨트롤러 - 지도 BBox 내 검색(+ 선택 검색어/기간) - 단건 상세 - 상태별 집계
 */
@RestController
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
}


