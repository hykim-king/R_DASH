package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TempMapper {
	
	List<Map<String, Object>> selectTop5PM10();

    List<Map<String, Object>> selectBottom5PM10();

    Double selectAvgPM10();
}
