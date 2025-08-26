package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.LandslideDTO;
import com.pcwk.ehr.mapper.LandslideMapper;

@Service
public class LandslideServiceImpl implements LandslideService {

    private final Logger log = LogManager.getLogger(getClass());

    @Autowired
    private LandslideMapper mapper;


    @Override
    public List<LandslideDTO> selectByBBox(double minLat, double maxLat,
                                           double minLon, double maxLon,
                                           String q, String year) {
        return mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q, year);
    }

    @Override
    public List<Map<String, Object>> countByRegionInBBox(double minLat, double maxLat,
                                                         double minLon, double maxLon,
                                                         String q, String year) {
        return mapper.countByRegionInBBox(minLat, maxLat, minLon, maxLon, q, year);
    }

    @Override
    public List<Map<String, Object>> countBySidoInBBox(double minLat, double maxLat,
                                                       double minLon, double maxLon,
                                                       String q, String year) {
        return mapper.countBySidoInBBox(minLat, maxLat, minLon, maxLon, q, year);
    }

    @Override
    public LandslideDTO findById(Long landslideNo) {
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
