package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.FirestationDTO;

//소방서 조회 전용 Mapper - 전체 목록 - 단건 조회 - BBox(지도 범위) 조회 - 지역명 / 소방서명 자동완성 - 특정 지역

@Mapper
public interface FirestationMapper  extends WorkDiv<FirestationDTO>{

//	전체 소방서 목록
	List<FirestationDTO> selectAll();

//    단건 조회
	FirestationDTO findById(@Param("stationNo") int stationNo);

//    특정 지역의 소방서 목록
	List<FirestationDTO> listByArea(@Param("area") String area);

//    지역명 자동완성(q 검색 키워드)
	List<String> suggestKeyword(@Param("q") String q);

//    소방서명 자동완성
	List<String> suggestStation(@Param("q") String q);

	/**
	 * 지도 BBox + 키워드 검색
	 * 
	 * @param minLat 최소 위도
	 * @param maxLat 최대 위도
	 * @param minLon 최소 경도
	 * @param maxLon 최대 경도
	 * @param q      검색 키워드(지역명/소방서명)
	 */
	List<FirestationDTO> selectByBBox(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
			@Param("minLon") double minLon, @Param("maxLon") double maxLon, @Param("q") String q);
}
