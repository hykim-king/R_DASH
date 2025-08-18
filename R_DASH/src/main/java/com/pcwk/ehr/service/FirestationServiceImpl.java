package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.FirestationDTO;
import com.pcwk.ehr.mapper.FirestationMapper;

@Service
public class FirestationServiceImpl implements FirestationService {

    @Autowired
    private FirestationMapper mapper;

    @Override
    public List<FirestationDTO> selectByBBox(double minLat, double maxLat,
                                             double minLon, double maxLon,
                                             String q, Integer limit) {
        return mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q, limit);
    }

    @Override
    public List<FirestationDTO> search(String q, String area,
                                       Double minLat, Double maxLat,
                                       Double minLon, Double maxLon,
                                       String orderBy, Integer limit, Integer offset) {
        return mapper.search(q, area, minLat, maxLat, minLon, maxLon, orderBy, limit, offset);
    }

    @Override
    public int countSearch(String q, String area,
                           Double minLat, Double maxLat,
                           Double minLon, Double maxLon) {
        return mapper.countSearch(q, area, minLat, maxLat, minLon, maxLon);
    }

    @Override
    public FirestationDTO selectOne(int stationNo) {
        return mapper.selectOne(stationNo);
    }

    @Override
    public List<String> autocompleteArea(String prefix, Integer limit) {
        return mapper.autocompleteArea(prefix, limit);
    }

    @Override
    public List<String> autocompleteStation(String prefix, String area, Integer limit) {
        return mapper.autocompleteStation(prefix, area, limit);

    public List<String> autocompleteStation(String prefix,  Integer limit) {
        return mapper.autocompleteStation(prefix,  limit);
    }
}
