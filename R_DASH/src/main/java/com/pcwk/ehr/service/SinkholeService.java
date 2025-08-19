package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import com.pcwk.ehr.domain.SinkholeDTO;

//SinkholeService.java
public interface SinkholeService {
	List<SinkholeDTO> selectByBBox(double minLat, double maxLat, double minLon, double maxLon);

	SinkholeDTO findById(Integer sinkholeNo);

	List<SinkholeDTO> selectAll();
	
    List<Map<String,Object>> countByState();
}
