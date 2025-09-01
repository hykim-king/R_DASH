// /src/main/java/com/pcwk/ehr/mapper/NowcastMapper.java
package com.pcwk.ehr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.domain.NowcastDTO;

@Mapper
public interface NowcastMapper {
	  // 1) 지도 색칠용(단일 카테고리 전국 최신)
    List<Map<String, Object>> selectNationLatest(@Param("category") String category);

    // 2) 전국: 모든 시군구 × 4카테고리 최신(지역당 4행)
    List<NowcastDTO> selectLatest4All();

    // 3) 특정 시군구 × 4카테고리 최신(항상 4행)
    List<NowcastDTO> selectLatest4ByRegion(@Param("sidoNm") String sidoNm,
                                           @Param("signguNm") String signguNm);
}
