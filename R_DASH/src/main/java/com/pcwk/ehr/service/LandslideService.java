package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import com.pcwk.ehr.domain.LandslideDTO;

public interface LandslideService {

    /** BBox 내 포인트(단건) 목록 */
    List<LandslideDTO> getPoints(double minLat, double maxLat,
                                 double minLon, double maxLon,
                                 String q);

    /** 버블 집계 (level=sgg|sido) */
    List<Map<String, Object>> getBubbles(double minLat, double maxLat,
                                         double minLon, double maxLon,
                                         String level, String q);

    /** 단건 상세 */
    LandslideDTO getDetail(Long landslideNo);

	List<Map<String, Object>> getByYear();

	List<Map<String, Object>> getByRegion();

	List<Map<String, Object>> getByMonth();

	List<Map<String, Object>> getByStatus();
}
