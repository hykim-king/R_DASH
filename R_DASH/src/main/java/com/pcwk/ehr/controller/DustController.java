package com.pcwk.ehr.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcwk.ehr.domain.DustDTO;
import com.pcwk.ehr.service.DustService;

/**
 * 프런트에서 airType 하나만 선택해서 호출
 * - BBox 파라미터가 모두 오면 BBox로
 * - 아니면 전국 조회
 */
@RestController
@RequestMapping("/api/dust")
public class DustController {

    private final DustService dustService;

    @Autowired
    public DustController(DustService dustService) {
        this.dustService = dustService;
    }

    @GetMapping("/latest")
    public List<DustDTO> latest(
            @RequestParam String airType,                   // ex) "교외대기", "도로변대기", "도시대기"
            @RequestParam(required = false) String day,     // ex) "2025-08-17" (옵션)
            @RequestParam(required = false) Double minLat,
            @RequestParam(required = false) Double maxLat,
            @RequestParam(required = false) Double minLon,
            @RequestParam(required = false) Double maxLon,
            @RequestParam(required = false, defaultValue = "500") Integer limit
    ) {
        boolean hasBBox = Objects.nonNull(minLat) && Objects.nonNull(maxLat)
                       && Objects.nonNull(minLon) && Objects.nonNull(maxLon);

        if (hasBBox) {
            return dustService.getLatestByTypeBBox(airType, day, minLat, maxLat, minLon, maxLon, limit);
        } else {
            return dustService.getLatestByTypeAll(airType, day, limit);
        }
    }
}