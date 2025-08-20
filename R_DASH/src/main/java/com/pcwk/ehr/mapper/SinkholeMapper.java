package com.pcwk.ehr.mapper;

import com.pcwk.ehr.domain.SinkholeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SinkholeMapper {

    List<SinkholeDTO> selectAll();

    List<SinkholeDTO> selectByBBox(
        @Param("minLat") double minLat, @Param("maxLat") double maxLat,
        @Param("minLon") double minLon, @Param("maxLon") double maxLon
    );

    // ServiceImpl.findById()와 정확히 이름/파라미터 일치
    SinkholeDTO findById(@Param("sinkholeNo") int sinkholeNo);

    // 상태별 집계 (STATE_NM별 count)
    List<Map<String, Object>> countByState();
    
    List<Map<String, Object>> selectYearlyCounts();
    
    List<Map<String, Object>> selectSignguCounts();
    
    List<Map<String, Object>> selectMonthlyCounts();
    
    List<Map<String, Object>> selectYearlyDamageStats();
}
