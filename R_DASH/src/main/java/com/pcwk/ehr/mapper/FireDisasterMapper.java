package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FireDisasterMapper {
	List<Map<String, Object>> selectYearlyFireCount();
    List<Map<String, Object>> selectFireTypeDamage();
}
