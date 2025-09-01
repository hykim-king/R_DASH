package com.pcwk.ehr.service;

import java.util.List;

import com.pcwk.ehr.domain.ShelterDTO;

public interface ShelterService {

	List<ShelterDTO> selectByBBox(double minLat, double maxLat, double minLon, double maxLon, String q, Integer limit)
			throws Exception;

	ShelterDTO selectOne(Integer shelterNo) throws Exception;

	List<ShelterDTO> selectList(Integer limit) throws Exception;

	List<String> suggestAdress(String q) throws Exception;

	List<String> suggestReareNm(String q) throws Exception;
}