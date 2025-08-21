package com.pcwk.ehr.service;

import com.pcwk.ehr.domain.SinkholeDTO;
import com.pcwk.ehr.mapper.SinkholeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SinkholeServiceImpl implements SinkholeService {

    @Autowired
    private SinkholeMapper mapper;

    @Override
    public List<SinkholeDTO> selectByBBox(double minLat, double maxLat,
                                          double minLon, double maxLon) {
        return mapper.selectByBBox(minLat, maxLat, minLon, maxLon);
    }

    @Override
    public SinkholeDTO findById(Integer sinkholeNo) {
        return mapper.findById(sinkholeNo);
    }

    @Override
    public List<Map<String, Object>> countByState() {
        return mapper.countByState();
    }

    @Override
    public List<SinkholeDTO> selectAll() {
        return mapper.selectAll();
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
