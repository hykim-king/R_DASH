package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.NowcastDTO;

public interface NowcastMapper extends WorkDiv<NowcastDTO> {
//	전체 초단기 목록
	List<NowcastDTO> selectAll();

//  단건 조회
	NowcastDTO selectSidoNm(@Param("sidoNm") String sidoNm);

}
