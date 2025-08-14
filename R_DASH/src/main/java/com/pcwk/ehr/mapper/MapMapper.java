package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.MapDTO;


@Mapper
public interface MapMapper extends WorkDiv<MapDTO>{
	/**
     * 공용 지도 마커 조회
     *
     * @param layer   레이어(SHELTER | FIRE_STATION | SINKHOLE | LANDSLIDE | DUST ...)
     * @param minLat  BBox 최소 위도
     * @param maxLat  BBox 최대 위도
     * @param minLon  BBox 최소 경도
     * @param maxLon  BBox 최대 경도
     * @param q       검색어(부분일치, null/빈문자 허용)
     * @param dateFrom yyyy-MM-dd 또는 yyyy-MM-dd HH:mm (옵션, 날짜 있는 레이어에만 적용)
     * @param dateTo   yyyy-MM-dd 또는 yyyy-MM-dd HH:mm (옵션)
     * @param limit   최대 개수(권장 200~500)
     */
    List<MapDTO> selectMarkersByBBox(
            @Param("layer") String layer,
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon,
            @Param("q") String q,
            @Param("dateFrom") String dateFrom,
            @Param("dateTo") String dateTo,
            @Param("limit") int limit
    );

    /**
     * 단건 상세
     * @param layer  레이어
     * @param id     원본 테이블 PK(= TARGET_NO)
     */
    MapDTO findDetail(
            @Param("layer") String layer,
            @Param("id") Integer id
    );

    /**
     * 자동완성(옵션)
     */
    List<String> suggestKeyword(
            @Param("layer") String layer,
            @Param("q") String q,
            @Param("limit") int limit
    );
    
    
}