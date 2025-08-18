package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FireMapper {

	List<Map<String, Object>> firestationCount();
	
	
}
