package com.pcwk.ehr.service;

import java.util.List;

import com.pcwk.ehr.domain.DustDTO;

public interface DustService {

	 List<DustDTO> getLatestByTypeBBox(
	            String airType, String day,
	            Double minLat, Double maxLat,
	            Double minLon, Double maxLon,
	            Integer limit
	    );

	    List<DustDTO> getLatestByTypeAll(
	            String airType, String day,
	            Integer limit
	    );
	}