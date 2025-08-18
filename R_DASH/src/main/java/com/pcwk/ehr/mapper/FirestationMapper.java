package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.FirestationDTO;


@Mapper
public interface FirestationMapper extends WorkDiv<FirestationDTO> {

    /** 지도용: BBox + 키워드(부분일치, null 허용) */
    List<FirestationDTO> selectByBBox(
        @Param("minLat") double minLat,
        @Param("maxLat") double maxLat,
        @Param("minLon") double minLon,
        @Param("maxLon") double maxLon,
        @Param("q") String q,
        @Param("limit") Integer limit
    );

    /** 키워드 검색 (옵션: 지역, BBox, 정렬, 페이징) */
    List<FirestationDTO> search(
        @Param("q") String q,
        @Param("area") String area,
        @Param("minLat") Double minLat,
        @Param("maxLat") Double maxLat,
        @Param("minLon") Double minLon,
        @Param("maxLon") Double maxLon,
        @Param("orderBy") String orderBy,   // "name" | "recent"
        @Param("limit") Integer limit,
        @Param("offset") Integer offset
    );

    /** 총건수 (페이지네이션용) */
    int countSearch(
        @Param("q") String q,
        @Param("area") String area,
        @Param("minLat") Double minLat,
        @Param("maxLat") Double maxLat,
        @Param("minLon") Double minLon,
        @Param("maxLon") Double maxLon
    );

    /** 단건 상세 */
    FirestationDTO selectOne(@Param("stationNo") int stationNo);

    /** 자동완성 - 지역명(prefix% 매칭) */
    List<String> autocompleteArea(@Param("prefix") String prefix,
                                  @Param("limit") Integer limit);

    /** 자동완성 - 소방서명(prefix% 매칭, 선택: 지역 필터) */
    List<String> autocompleteStation(@Param("prefix") String prefix,
                                     @Param("limit") Integer limit);
}
