package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.mapper.FireExitMapper;

@Service
public class FireExitServiceImpl implements FireExitService {

	@Autowired
    private FireExitMapper fireExitMapper;

    @Override
    public List<Map<String,Object>> getSubwayExtCount() {
        return fireExitMapper.selectSubwayFireExtCount();
    }

}
