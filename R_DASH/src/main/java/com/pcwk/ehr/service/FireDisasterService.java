package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

public interface FireDisasterService {
	
	 List<Map<String, Object>> getYearlyFireCount();
	 List<Map<String, Object>> getFireTypeDamage();
}
