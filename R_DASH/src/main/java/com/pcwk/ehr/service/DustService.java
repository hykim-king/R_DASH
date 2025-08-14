package com.pcwk.ehr.service;

import java.util.List;

import com.pcwk.ehr.domain.DustDTO;

public interface DustService {

//	 지도 BBox + (선택)기간 - BBox: 현재 지도 뷰포트 안의 측정소만 - dateFrom/dateTo: 'YYYY-MM-DD' 또는
//	'YYYY-MM-DD HH24:MI' 형식 가변 처리 (XML에서) - q: 지역명/측정소명 부분일치(선택)
	List<DustDTO> getByBBox(double minLat, double maxLat, double minLon, double maxLon, String dateFrom, String dateTo);

//  특정 측정소의 최신 1건
	DustDTO getLatestByStation(String stnNm);
	
	List<DustDTO> getLatestEachStationInBBox(
		    double minLat, double maxLat, double minLon, double maxLon,
		    String dateFrom, String dateTo, String urbanType, String q);
}
