package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import com.pcwk.ehr.domain.LandslideDTO;

public interface LandslideService {

    // 지도 시각화용
    List<LandslideDTO> selectByBBox(double minLat, double maxLat,
                                    double minLon, double maxLon,
                                    String q, String year);

    List<Map<String,Object>> countByRegionInBBox(double minLat, double maxLat,
                                                 double minLon, double maxLon,
                                                 String q, String year);

    List<Map<String,Object>> countBySidoInBBox(double minLat, double maxLat,
                                               double minLon, double maxLon,
                                               String q, String year);

    LandslideDTO findById(Long landslideNo);

	List<Map<String, Object>> getByYear();

	List<Map<String, Object>> getByRegion();

	List<Map<String, Object>> getByMonth();

	List<Map<String, Object>> getByStatus();
}
