package com.pcwk.ehr.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.PatientsDTO;

@Mapper
public interface PatientsMapper extends WorkDiv<PatientsDTO>{
	
	//지역별 온열질환자 통계
	public PatientsDTO getPatientsAreaStat();
		
	//년도별 온열질환자 통계
	public PatientsDTO getPatientsYearAreaStat();

	//실내외 온열질환자 통계
	public PatientsDTO getPatientsYearStat();
}
