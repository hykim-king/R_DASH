package com.pcwk.ehr.service;

import java.util.List;

import com.pcwk.ehr.domain.PatientsDTO;

public interface TemperatureService {

	void savePatient(PatientsDTO dto);
    List<PatientsDTO> getAllPatients();
    void fetchAndSaveData();
}
