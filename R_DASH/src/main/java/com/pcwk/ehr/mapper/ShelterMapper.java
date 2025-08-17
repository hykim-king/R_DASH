package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.ShelterDTO;

@Mapper
public interface ShelterMapper extends WorkDiv<ShelterDTO> {
	/** 단건 상세 */
	ShelterDTO selectOne(@Param("shelterNo") long shelterNo);

	/** 단순 리스트(상위 N개) */
	List<ShelterDTO> selectList(@Param("limit") Integer limit);

	/** 자동완성 - 주소 */
	List<String> suggestAdress(@Param("q") String q);

	/** 자동완성 - 시설명 */
	List<String> suggestReareNm(@Param("q") String q);

	/**
	 * 지도 BBox + 키워드 검색
	 * 
	 * @param minLat 최소 위도
	 * @param maxLat 최대 위도
	 * @param minLon 최소 경도
	 * @param maxLon 최대 경도
	 * @param q      검색 키워드(지역명/대피소명)
	 */
	List<ShelterDTO> selectByBBox(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
			@Param("minLon") double minLon, @Param("maxLon") double maxLon, @Param("q") String q,
			@Param("limit") Integer limit);
}
