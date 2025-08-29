package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import com.pcwk.ehr.domain.FirestationDTO;

public interface FirestationService {

	List<FirestationDTO> selectByBBox(double minLat, double maxLat, double minLon, double maxLon, int limit,
			String fireTp, String fireTpLabel);

	List<FirestationDTO> search(String q, int limit, int offset, String fireTp, String fireTpLabel);

	int countSearch(String q, String area, Double minLat, Double maxLat, Double minLon, Double maxLon, String fireTp);

	FirestationDTO selectOne(int stationNo);

	// 자동완성
	List<String> autocompleteArea(String prefix, Integer limit, String fireTp);

	List<String> autocompleteStation(String prefix, String area, Integer limit, String fireTp);

	List<Map<String, Object>> getfirestationCounts();

	List<Map<String, Object>> getSigunguFireCounts();

}