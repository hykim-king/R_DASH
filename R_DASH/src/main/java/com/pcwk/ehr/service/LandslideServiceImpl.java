package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import com.pcwk.ehr.domain.LandslideDTO;
import com.pcwk.ehr.mapper.LandslideMapper;

@Service
public class LandslideServiceImpl implements LandslideService {

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private LandslideMapper mapper;

    @Override
    public List<LandslideDTO> getPoints(double minLat, double maxLat,
                                        double minLon, double maxLon,
                                        String q) {
        log.debug("getPoints bbox=({},{} ~ {},{}), q={}", minLat, minLon, maxLat, maxLon, q);
        return mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q);
    }

    @Override
    public List<Map<String, Object>> getBubbles(double minLat, double maxLat,
                                                double minLon, double maxLon,
                                                String level, String q) {
        log.debug("getBubbles level={}, bbox=({},{} ~ {},{}), q={}",
                level, minLat, minLon, maxLat, maxLon, q);
        if ("sido".equalsIgnoreCase(level)) {
            return mapper.countBySidoInBBox(minLat, maxLat, minLon, maxLon, q);
        }
        return mapper.countByRegionInBBox(minLat, maxLat, minLon, maxLon, q);
    }

    @Override
    public LandslideDTO getDetail(Long landslideNo) {
        log.debug("getDetail id={}", landslideNo);
        return mapper.findById(landslideNo);
    }
    
    @Override
    public List<Map<String,Object>> getByYear() {
    	return mapper.selectByYear();
    }
    
    @Override
    public List<Map<String,Object>> getByRegion() {
    	return mapper.selectByRegion();
    }
    
    @Override
    public List<Map<String,Object>> getByMonth() {
    	return mapper.selectByMonth();
    }
    
    @Override
    public List<Map<String,Object>> getByStatus() {
    	return mapper.selectByStatus();
    }
    
}
