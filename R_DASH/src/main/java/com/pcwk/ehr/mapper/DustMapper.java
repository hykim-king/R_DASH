package com.pcwk.ehr.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.DustDTO;

@Mapper
public interface DustMapper extends WorkDiv<DustDTO> {

	/**
     * (지도화면용) BBox 안에서 airType의 최신 1건/측정소
     */
    List<DustDTO> selectLatestByTypeBBox(
            @Param("airType") String airType,
            @Param("day") String day,              // "YYYY-MM-DD" (옵션)
            @Param("minLat") Double minLat,
            @Param("maxLat") Double maxLat,
            @Param("minLon") Double minLon,
            @Param("maxLon") Double maxLon,
            @Param("limit") Integer limit          // 옵션
    );

    /**
     * (전국) airType의 최신 1건/측정소
     */
    List<DustDTO> selectLatestByTypeAll(
            @Param("airType") String airType,
            @Param("day") String day,              // "YYYY-MM-DD" (옵션)
            @Param("limit") Integer limit          // 옵션
    );
}