package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.mapper.FireMapper;

public interface FireService {

	List<Map<String, Object>> getfirestationCounts();

	List<Map<String, Object>> getSelectFireSafe();
	
}
