package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.mapper.FireSafeMapper;

@Service
public class FireSafeServiceImpl implements FireSafeService {

	@Autowired
	FireSafeMapper fireSafeMapper;
	
	@Override
	public List<Map<String, Object>> getSelectFireSafe() {
        return fireSafeMapper.SelectFireSafe();	
    }

}
