package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.LandslideDTO;


@Mapper
public interface LandslideMapper extends WorkDiv<LandslideDTO> {

	
	 List<Integer> selectYears();

	    List<LandslideDTO> selectByBBox(
	        @Param("minLat") double minLat, @Param("maxLat") double maxLat,
	        @Param("minLon") double minLon, @Param("maxLon") double maxLon,
	        @Param("q") String q, @Param("year") String year
	    );

	    List<Map<String,Object>> countByRegionInBBox(
	        @Param("minLat") double minLat, @Param("maxLat") double maxLat,
	        @Param("minLon") double minLon, @Param("maxLon") double maxLon,
	        @Param("q") String q, @Param("year") String year
	    );

	    List<Map<String,Object>> countBySidoInBBox(
	        @Param("minLat") double minLat, @Param("maxLat") double maxLat,
	        @Param("minLon") double minLon, @Param("maxLon") double maxLon,
	        @Param("q") String q, @Param("year") String year
	    );

	    LandslideDTO findById(@Param("landslideNo") Long landslideNo);
	    
	    
	    
	    
	    
    
    List<Map<String,Object>> selectByYear();
    
    List<Map<String,Object>> selectByRegion();
    
    List<Map<String,Object>> selectByMonth();
    
    List<Map<String,Object>> selectByStatus();
}
