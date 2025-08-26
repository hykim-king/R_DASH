// /src/main/java/com/pcwk/ehr/controller/NowcastController.java
package com.pcwk.ehr.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.service.NowcastService;

@RestController
@RequestMapping("/nowcast")
public class NowcastController {

    @Autowired
    NowcastService nowcastService;
    
    
    
    /** 1) 지도 색칠용: 단일 카테고리 전국 최신 스냅샷 */
    @GetMapping("/latest.do")
    public List<Map<String, Object>> latestNation(@RequestParam String category) {
        // category: T1H | RN1 | REH | WSD (대소문자 무관하게 서버 쿼리에서 처리)
        return nowcastService.getNationLatest(category);
    }

    /** 2) 전국: 모든 시군구 × 4카테고리 최신(지역당 4행) */
    @GetMapping("/latest4")
    public List<NowcastDTO> latest4All() {
        return nowcastService.getLatest4All();
    }

    /** 3) 특정 시군구 × 4카테고리 최신(항상 4행) */
    @GetMapping("/latest4-region")
    public List<NowcastDTO> latest4Region(@RequestParam String sidoNm,
                                          @RequestParam String signguNm) {
        return nowcastService.getLatest4ByRegion(sidoNm, signguNm);
    }
}
