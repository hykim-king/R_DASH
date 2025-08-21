package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.DustDTO;

@Mapper
public interface DustMapper extends WorkDiv<DustDTO> {

    // BBox 안에서 airType의 최신 1건/측정소
    List<DustDTO> selectLatestByTypeBBox(
    		 @Param("airType") String airType,
    	        @Param("day")     String day,
    	        @Param("minLat")  Double minLat,
    	        @Param("maxLat")  Double maxLat,
    	        @Param("minLon")  Double minLon,
    	        @Param("maxLon")  Double maxLon,
    	        @Param("limit")   Integer limit
    );

    // 전국 airType 최신 1건/측정소
    List<DustDTO> selectLatestByTypeAll(
    		@Param("airType") String airType,
            @Param("day")     String day,
            @Param("limit")   Integer limit
    );
    
    List<Map<String, Object>> selectTop5PM10();

    List<Map<String, Object>> selectBottom5PM10();

    Double selectAvgPM10();
}