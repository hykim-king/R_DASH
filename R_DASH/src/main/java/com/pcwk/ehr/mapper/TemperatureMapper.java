package com.pcwk.ehr.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.domain.PatientsDTO;

@Mapper
public interface TemperatureMapper {
	
	int upsertNowcast(List<NowcastDTO> list);
	
	int insertPatient(PatientsDTO dto) throws SQLException;
	
    List<PatientsDTO> selectAllPatients() throws SQLException;
	
    PatientsDTO selectSidoPatients(String param) throws SQLException;
    
    void deleteAll() throws SQLException;

	int getCount() throws SQLException;
	
	List<PatientsDTO> selectPatientsSummary(Map<String, Object> map) throws SQLException;
	
	//드롭다운 만들기 위해 값가져오기
	List<String> pSido() throws SQLException;
	List<String> pYear() throws SQLException;
}
