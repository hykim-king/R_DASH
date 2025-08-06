package com.pcwk.ehr.service;

import java.sql.SQLException;
import java.util.List;

import com.pcwk.ehr.domain.PatientsDTO;

public interface TemperatureService {

	void savePatient(PatientsDTO dto) throws SQLException;
    List<PatientsDTO> getAllPatients() throws SQLException;
    String fetchAndSaveData() throws SQLException;
}
