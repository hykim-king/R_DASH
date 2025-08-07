package com.pcwk.ehr.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.domain.PatientsDTO;

@Mapper
public interface TemperatureMapper {
	
	int insertNowcast(NowcastDTO nowcast) throws SQLException;
	
	int insertPatient(PatientsDTO patient) throws SQLException;
	
    List<PatientsDTO> selectAllPatients() throws SQLException;
	
    PatientsDTO selectSidoPatients(String param) throws SQLException;
    
    void deleteAll() throws SQLException;

	int getCount() throws SQLException;
}
