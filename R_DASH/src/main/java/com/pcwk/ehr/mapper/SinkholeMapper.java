package com.pcwk.ehr.mapper;

import com.pcwk.ehr.domain.SinkholeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SinkholeMapper {

	// 1) 히트맵 전체 포인트
    List<Map<String, Object>> selectAllPoints(
            @Param("year") String year,
            @Param("stateNm") String stateNm
    );

    // 2) 히트맵 BBox 포인트
    List<SinkholeDTO> selectPointsByBBox(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon,
            @Param("year") String year,
            @Param("stateNm") String stateNm
    );

    // 3) 버블 BBox 집계
    List<SinkholeDTO> selectBubblesByBBox(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon,
            @Param("round") int round,
            @Param("year") String year,
            @Param("stateNm") String stateNm
    );

    // 4) 버블 시·군·구 집계
    List<SinkholeDTO> selectBubbleStats(
            @Param("sido") String sido,
            @Param("year") String year,
            @Param("stateNm") String stateNm
    );

    // 상태별 집계 (STATE_NM별 count)
    List<Map<String, Object>> countByState();
    
    List<Map<String, Object>> selectYearlyCounts();
    
    List<Map<String, Object>> selectSignguCounts();
    
    List<Map<String, Object>> selectMonthlyCounts();
    
    List<Map<String, Object>> selectYearlyDamageStats();
}
