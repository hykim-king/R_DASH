package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.NowcastDTO;

@Mapper
public interface NowcastMapper extends WorkDiv<NowcastDTO> {

    /**
     * 최신 “공통 시각” (T1H/RN1/WSD/REH 모두 존재하는 가장 최근 시각 1건)
     * @return BASE_DATE, BASE_TIME가 key인 Map
     */
    Map<String, String> selectLatestCommonBase();

    /**
     * 특정 (BASE_DATE, BASE_TIME)의 전국 데이터
     */
    List<NowcastDTO> selectNowcastByBase(
        @Param("baseDate") String baseDate,
        @Param("baseTime") String baseTime
    );

    /**
     * 특정 시도/시군구 데이터 (해당 시각의 4행: T1H, RN1, WSD, REH)
     */
    List<NowcastDTO> selectNowcastByRegion(
        @Param("baseDate") String baseDate,
        @Param("baseTime") String baseTime,
        @Param("sidoNm") String sidoNm,
        @Param("signguNm") String sigunguNm
    );

public interface NowcastMapper extends WorkDiv<NowcastDTO> {
//	전체 초단기 목록
	List<NowcastDTO> selectAll();

//  단건 조회
	NowcastDTO selectSidoNm(@Param("sidoNm") String sidoNm);

}
