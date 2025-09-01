package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.FirestationDTO;

@Mapper
public interface FirestationMapper extends WorkDiv<FirestationDTO> {

	List<FirestationDTO> selectByBBox(Map<String, Object> params);

	List<FirestationDTO> search(Map<String, Object> params);

	int countSearch(@Param("q") String q, @Param("area") String area, @Param("minLat") Double minLat,
			@Param("maxLat") Double maxLat, @Param("minLon") Double minLon, @Param("maxLon") Double maxLon,
			@Param("fireTp") String fireTp);

	FirestationDTO selectOne(@Param("stationNo") int stationNo);

	/** 자동완성 - 지역명(prefix% 매칭) */
	List<String> autocompleteArea(@Param("prefix") String prefix, @Param("limit") Integer limit,
			@Param("fireTp") String fireTp);

	List<String> autocompleteStation(@Param("prefix") String prefix, @Param("area") String area,
			@Param("limit") Integer limit, @Param("fireTp") String fireTp);

	/**
	 * 소방서 수
	 * 
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> firestationCount();

	List<Map<String, Object>> sigunguFireCount();

}