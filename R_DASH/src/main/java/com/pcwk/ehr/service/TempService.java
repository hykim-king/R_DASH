package com.pcwk.ehr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.mapper.DustMapper;
import com.pcwk.ehr.mapper.TempMapper;

@Service
public class TempService {

	
	@Autowired
	private TempMapper tempMapper;

    public TempService(TempMapper tempMapper) {
        this.tempMapper = tempMapper;
    }

    public Map<String, Object> getDustStats() {
        Map<String, Object> result = new HashMap<>();

        List<Map<String, Object>> top5 = tempMapper.selectTop5PM10();
        List<Map<String, Object>> bottom5 = tempMapper.selectBottom5PM10();
        Double avg = tempMapper.selectAvgPM10();

        result.put("top5", top5);
        result.put("bottom5", bottom5);
        result.put("avg", avg);

        return result;
    }
    
}
