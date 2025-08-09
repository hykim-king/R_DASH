package com.pcwk.ehr.service;

import java.sql.SQLException;
import java.util.List;

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.domain.PatientsDTO;

public interface TemperatureService {

	void saveNowcast(NowcastDTO dto) throws SQLException;
	
	void savePatient(PatientsDTO dto) throws SQLException;
	
    List<PatientsDTO> getAllPatients() throws SQLException;
    
    void insertPatient() throws SQLException;

}
