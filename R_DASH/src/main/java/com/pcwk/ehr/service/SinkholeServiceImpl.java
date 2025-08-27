package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.SinkholeDTO;
import com.pcwk.ehr.mapper.SinkholeMapper;

@Service
public class SinkholeServiceImpl implements SinkholeService {

	@Autowired
    private SinkholeMapper mapper;

    // 공통 트리밍 헬퍼
    private String clean(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    @Override
    public List<Map<String, Object>> selectAllPoints(String year, String stateNm) {
        return mapper.selectAllPoints(clean(year), clean(stateNm));
    }

    @Override
    public List<SinkholeDTO> selectPointsByBBox(double minLat, double maxLat,
                                                double minLon, double maxLon,
                                                String year, String stateNm) {
        return mapper.selectPointsByBBox(
                minLat, maxLat, minLon, maxLon,
                clean(year), clean(stateNm)
        );
    }

    @Override
    public List<SinkholeDTO> selectBubblesByBBox(double minLat, double maxLat,
                                                 double minLon, double maxLon,
                                                 int round, String year, String stateNm) {
        int r = (round < 1) ? 4 : round; // 기본 가드
        return mapper.selectBubblesByBBox(
                minLat, maxLat, minLon, maxLon,
                r, clean(year), clean(stateNm)
        );
    }

    @Override
    public List<SinkholeDTO> selectBubbleStats(String sido, String year, String stateNm) {
        return mapper.selectBubbleStats(
                clean(sido), clean(year), clean(stateNm)
        );
    }
    @Override
    public List<Map<String, Object>> countByState() {
        return mapper.countByState();
    }

    
    @Override
    public List<Map<String, Object>> getYearlyCounts() {
        return mapper.selectYearlyCounts();
    }

    @Override
    public List<Map<String, Object>> getSignguCounts() {
        return mapper.selectSignguCounts();
    }

    @Override
    public List<Map<String, Object>> getMonthlyCounts() {
        return mapper.selectMonthlyCounts();
    }

    @Override
    public List<Map<String, Object>> getYearlyDamageStats() {
        return mapper.selectYearlyDamageStats();
    }
}
