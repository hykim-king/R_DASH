package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.DustDTO;
import com.pcwk.ehr.mapper.DustMapper;

@Service
public class DustServiceImpl implements DustService {

    @Autowired
    private DustMapper dustMapper;

    @Autowired
    public DustServiceImpl(DustMapper dustMapper) {
        this.dustMapper = dustMapper;
    }

    @Override
    public List<DustDTO> getLatestByTypeBBox(String airType, String day,
                                             Double minLat, Double maxLat,
                                             Double minLon, Double maxLon,
                                             Integer limit) {
        return dustMapper.selectLatestByTypeBBox(
                airType, day, minLat, maxLat, minLon, maxLon, limit
        );
    }

    @Override
    public List<DustDTO> getLatestByTypeAll(String airType, String day, Integer limit) {
        return dustMapper.selectLatestByTypeAll(airType, day, limit);
    }
}