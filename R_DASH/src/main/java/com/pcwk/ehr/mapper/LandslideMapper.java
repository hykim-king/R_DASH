package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.LandslideDTO;


@Mapper
public interface LandslideMapper extends WorkDiv<LandslideDTO> {

	
	//BBOX 내 산사태 목록(포인트)
    List<LandslideDTO> selectByBBox(
        @Param("minLat") double minLat,
        @Param("maxLat") double maxLat,
        @Param("minLon") double minLon,
        @Param("maxLon") double maxLon,
        @Param("q") String q
    );

    
    // 단건 조회 ( 상세 ) 
    LandslideDTO findById(@Param("landslideNo") Long landslideNo);

    
    //BBOX 내 시군구 단위 집계( 버븐맵에 사용 ) 
    List<Map<String, Object>> countByRegionInBBox(
        @Param("minLat") double minLat,
        @Param("maxLat") double maxLat,
        @Param("minLon") double minLon,
        @Param("maxLon") double maxLon,
        @Param("q") String q
    );

    // 선택: 시/도 단위 집계
    List<Map<String,Object>> countBySidoInBBox(
        @Param("minLat") double minLat,
        @Param("maxLat") double maxLat,
        @Param("minLon") double minLon,
        @Param("maxLon") double maxLon,
        @Param("q") String q
    );
}
