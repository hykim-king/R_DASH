package com.pcwk.ehr.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.FireExitDTO;

@Mapper
public interface FireExitMapper extends WorkDiv<FireExitDTO> {
	
	int insertFire(FireExitDTO fire);
	
	int deleteFire(FireExitDTO fire);
	
	int deleteAll();
	
	int updateFire(FireExitDTO fire);
	
	
	
}
