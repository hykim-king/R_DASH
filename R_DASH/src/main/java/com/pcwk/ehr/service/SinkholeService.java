package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import com.pcwk.ehr.domain.SinkholeDTO;

//SinkholeService.java
public interface SinkholeService {
	// 히트맵 전체 포인트 (옵션: year, stateNm)
    List<Map<String, Object>> selectAllPoints(String year, String stateNm);

    // 히트맵 BBox 포인트 (옵션: year, stateNm)
    List<SinkholeDTO> selectPointsByBBox(double minLat, double maxLat,
                                         double minLon, double maxLon,
                                         String year, String stateNm);

    // 버블 BBox 집계 (옵션: year, stateNm)
    List<SinkholeDTO> selectBubblesByBBox(double minLat, double maxLat,
                                          double minLon, double maxLon,
                                          int round, String year, String stateNm);

    // 버블 시·군·구 집계 (옵션: sido, year, stateNm)
    List<SinkholeDTO> selectBubbleStats(String sido, String year, String stateNm);
	
	
	
	List<Map<String, Object>> countByState();

	List<Map<String, Object>> getYearlyCounts();

	List<Map<String, Object>> getSignguCounts();

	List<Map<String, Object>> getMonthlyCounts();

	List<Map<String, Object>> getYearlyDamageStats();

	
}
