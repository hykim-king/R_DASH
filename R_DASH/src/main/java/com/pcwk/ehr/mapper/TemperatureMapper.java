package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.domain.PatientsDTO;

@Mapper
public interface TemperatureMapper {
	
	int insertPatient(PatientsDTO patient);
	
    List<PatientsDTO> selectAllPatients();
	
}
