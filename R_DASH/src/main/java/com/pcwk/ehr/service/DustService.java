package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import com.pcwk.ehr.domain.DustDTO;

public interface DustService {

	/**
	 * 특정 대기유형 및 BBox(지리 경계) 기준 최신 데이터 조회
	 */
	 List<DustDTO> getLatestByTypeBBox(String airType, String day,
             Double minLat, Double maxLat,
             Double minLon, Double maxLon,
             Integer limit);

	/**
	 * 특정 대기유형 기준 전체 지역의 최신 데이터 조회
	 */
	 List<DustDTO> getLatestByTypeAll(String airType, String day, Integer limit);

	List<Map<String, Object>> getTop5PM10();

	List<Map<String, Object>> getBottom5PM10();

	Double getAvgPM10();
}