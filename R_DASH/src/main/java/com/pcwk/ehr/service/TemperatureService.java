package com.pcwk.ehr.service;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.domain.PatientsDTO;

public interface TemperatureService {
	
	void mergeNowcast(List<NowcastDTO> list);
	
    List<PatientsDTO> getAllPatients() throws SQLException;
    
    void insertPatient() throws SQLException;
    
    void insertNowcast() throws SQLException;
    
    void fetchAndMergeNowcast() throws URISyntaxException;

	List<PatientsDTO> selectPatientsSummary(Map<String, Object> map) throws SQLException;

	List<String> getSidoList() throws SQLException;

	List<String> getYearList() throws SQLException;
	
	List<NowcastDTO> getTopT1H();
	List<NowcastDTO> getTopRN1();
    List<NowcastDTO> getTopREH();
   

}
