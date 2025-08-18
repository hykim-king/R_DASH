package com.pcwk.ehr.service;

import java.util.List;

import com.pcwk.ehr.domain.FirestationDTO;

public interface FirestationService {

    // 지도용: BBox + 키워드
    List<FirestationDTO> selectByBBox(double minLat, double maxLat,
                                      double minLon, double maxLon,
                                      String q, Integer limit);

    // 검색(옵션: 지역, BBox, 정렬, 페이징)
    List<FirestationDTO> search(String q, String area,
                                Double minLat, Double maxLat,
                                Double minLon, Double maxLon,
                                String orderBy, Integer limit, Integer offset);

    int countSearch(String q, String area,
                    Double minLat, Double maxLat,
                    Double minLon, Double maxLon);

    // 단건 상세
    FirestationDTO selectOne(int stationNo);

    // 자동완성
    List<String> autocompleteArea(String prefix, Integer limit);
    List<String> autocompleteStation(String prefix, String area, Integer limit);
    List<String> autocompleteStation(String prefix, Integer limit);
}
