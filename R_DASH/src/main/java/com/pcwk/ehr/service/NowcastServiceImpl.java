// /src/main/java/com/pcwk/ehr/service/NowcastServiceImpl.java
package com.pcwk.ehr.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.mapper.NowcastMapper;

@Service
public class NowcastServiceImpl implements NowcastService {

	@Autowired
	NowcastMapper nowcastMapper;

	@Override
	public List<Map<String, Object>> getNationLatest(String category) {
		// TODO Auto-generated method stub
		return nowcastMapper.selectNationLatest(category);
	}

	@Override
	public List<NowcastDTO> getLatest4All() {
		// TODO Auto-generated method stub
		return nowcastMapper.selectLatest4All();
	}

	@Override
	public List<NowcastDTO> getLatest4ByRegion(String sidoNm, String signguNm) {
		// TODO Auto-generated method stub
		return nowcastMapper.selectLatest4ByRegion(sidoNm, signguNm);
	}
	
	

}
