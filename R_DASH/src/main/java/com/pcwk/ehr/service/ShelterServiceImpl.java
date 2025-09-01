package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.ShelterDTO;
import com.pcwk.ehr.mapper.ShelterMapper;

/** 대피소 Service 구현체 */
@Service
public class ShelterServiceImpl implements ShelterService {

    private final ShelterMapper mapper;

    @Autowired
    public ShelterServiceImpl(ShelterMapper mapper) {
        this.mapper = mapper;
    }

    /** 지도 뷰포트 조회 */
    @Override
    public List<ShelterDTO> selectByBBox(double minLat, double maxLat,
                                         double minLon, double maxLon,
                                         String q, Integer limit) throws Exception {
        return mapper.selectByBBox(minLat, maxLat, minLon, maxLon, q, limit);
    }

    /** 단건 상세 */
    @Override
    public ShelterDTO selectOne(Integer shelterNo) throws Exception {
        return mapper.selectOne(shelterNo);
    }

    /** 리스트 */
    @Override
    public List<ShelterDTO> selectList(Integer limit) throws Exception {
        return mapper.selectList(limit);
    }

    /** 자동완성 - 주소 */
    @Override
    public List<String> suggestAdress(String q) throws Exception {
        return mapper.suggestAdress(q);
    }

    /** 자동완성 - 시설명 */
    @Override
    public List<String> suggestReareNm(String q) throws Exception {
        return mapper.suggestReareNm(q);
    }
}