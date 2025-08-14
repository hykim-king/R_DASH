package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.SinkholeDTO;


@Mapper
public interface SinkholeMapper extends WorkDiv<SinkholeDTO> {
	
//지도 뷰포트(BBox) + 선택검색
	List<SinkholeDTO> selectByBBox(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
			@Param("minLon") double minLon, @Param("maxLon") double maxLon, @Param("q") String q,
			@Param("from") String from, @Param("to") String to);

//	단건 상세 
	SinkholeDTO findById(@Param("sinkholeNo") Integer sinkholeNo);
	
	//범례/필터용 상태별 건수 집계
	List<Map<String, Object>> countByState();
}
