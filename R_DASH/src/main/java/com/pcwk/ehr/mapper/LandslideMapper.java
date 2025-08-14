package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.LandslideDTO;


@Mapper
public interface LandslideMapper extends WorkDiv<LandslideDTO>{

	// BBox(지도 범위) 내 산사태 목록 조회
	    List<LandslideDTO> selectByBBox(
	            @Param("minLat") double minLat,
	            @Param("maxLat") double maxLat,
	            @Param("minLon") double minLon,
	            @Param("maxLon") double maxLon,
	            @Param("q") String q
	    );
	    
//	    단건 상세 조회 
	    LandslideDTO findById(@Param("landslideNo") Long landslideNo);

//	     BBox 내 지역별 발생 건수 집계 (버블맵용)
	    List<Map<String, Object>> countByRegionInBBox(
	            @Param("minLat") double minLat,
	            @Param("maxLat") double maxLat,
	            @Param("minLon") double minLon,
	            @Param("maxLon") double maxLon,
	            @Param("q") String q
	    );
}
