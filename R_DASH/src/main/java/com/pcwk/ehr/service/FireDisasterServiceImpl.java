package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.mapper.FireDisasterMapper;

@Service
public class FireDisasterServiceImpl implements FireDisasterService {
	
	@Autowired
	private FireDisasterMapper mapper;

    @Override
    public List<Map<String, Object>> getYearlyFireCount() {
        return mapper.selectYearlyFireCount();
    }

    @Override
    public List<Map<String, Object>> getFireTypeDamage() {
        return mapper.selectFireTypeDamage();
    }

}
