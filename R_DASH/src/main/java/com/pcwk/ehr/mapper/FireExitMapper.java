package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.FireExitDTO;

@Mapper
public interface FireExitMapper extends WorkDiv<FireExitDTO> {

	// PK 단건 조회

	FireExitDTO selectById(@Param("exitNo") Long exitNo);

	List<FireExitDTO> selectList(@Param("subNo") String subNo, @Param("subName") String subName,
			@Param("udGr") String udGr, @Param("locPl") String locPl);

	int count();

	// 읽기 전용이여서 WorkDiv받으면 호출될 수 있기 때문에 UnsupportedOperationException 써서 읽지 않도록
	// 막아놓았음.
	default int doSave(FireExitDTO vo) {
		throw new UnsupportedOperationException("READ-ONLY");
	}

	default int doUpdate(FireExitDTO vo) {
		throw new UnsupportedOperationException("READ-ONLY");
	}

	default int doDelete(FireExitDTO vo) {
		throw new UnsupportedOperationException("READ-ONLY");
	}
}