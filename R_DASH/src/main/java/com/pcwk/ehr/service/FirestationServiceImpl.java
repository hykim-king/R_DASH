package com.pcwk.ehr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.FirestationDTO;
import com.pcwk.ehr.mapper.FireSafeMapper;
import com.pcwk.ehr.mapper.FirestationMapper;

@Service
public class FirestationServiceImpl implements FirestationService {

	@Autowired
	private FirestationMapper mapper;

	@Override
	public List<FirestationDTO> selectByBBox(double minLat, double maxLat, double minLon, double maxLon, int limit,
			String fireTp, String fireTpLabel) {

		Map<String, Object> p = new HashMap<>();
		p.put("minLat", minLat);
		p.put("maxLat", maxLat);
		p.put("minLon", minLon);
		p.put("maxLon", maxLon);
		p.put("limit", limit);
		p.put("fireTp", fireTp);
		p.put("fireTpLabel", fireTpLabel);
		return mapper.selectByBBox(p);
	}

	@Override
	public List<FirestationDTO> search(String q, int limit, int offset, String fireTp, String fireTpLabel) {

		Map<String, Object> p = new HashMap<>();
		p.put("q", q);
		p.put("limit", limit);
		p.put("offset", offset);
		p.put("fireTp", fireTp);
		p.put("fireTpLabel", fireTpLabel);
		return mapper.search(p);
	}

	@Override
	public int countSearch(String q, String area, Double minLat, Double maxLat, Double minLon, Double maxLon,
			String fireTp) {
		return mapper.countSearch(q, area, minLat, maxLat, minLon, maxLon, fireTp);
	}

	@Override
	public FirestationDTO selectOne(int stationNo) {
		return mapper.selectOne(stationNo);
	}

	// -------- 자동완성 (fireTp 포함 버전) --------
	@Override
	public List<String> autocompleteArea(String prefix, Integer limit, String fireTp) {
		return mapper.autocompleteArea(prefix, limit, fireTp);
	}

	@Override
	public List<String> autocompleteStation(String prefix, String area, Integer limit, String fireTp) {
		return mapper.autocompleteStation(prefix, area, limit, fireTp);
	}

	@Override
	public List<Map<String, Object>> getfirestationCounts() {
		return mapper.firestationCount();
	}

	@Override
	public List<Map<String, Object>> getSigunguFireCounts() {
		return mapper.sigunguFireCount();
	}

}