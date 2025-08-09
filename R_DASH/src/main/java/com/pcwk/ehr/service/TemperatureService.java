package com.pcwk.ehr.service;

import java.sql.SQLException;
import java.util.List;

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.domain.PatientsDTO;

public interface TemperatureService {
	
    List<PatientsDTO> getAllPatients() throws SQLException;
    
    void insertPatient() throws SQLException;
    
    void insertNowcast() throws SQLException;

}
