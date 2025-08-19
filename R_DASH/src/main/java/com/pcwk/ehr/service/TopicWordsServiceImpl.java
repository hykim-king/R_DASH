package com.pcwk.ehr.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.TopicWordsDTO;
import com.pcwk.ehr.mapper.TopicWordsMapper;

@Service
public class TopicWordsServiceImpl implements TopicWordsService {

	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	TopicWordsMapper wordsMapper;
	
	@Override
	public List<TopicWordsDTO> top150(TopicWordsDTO param) {
		// TODO Auto-generated method stub
		return wordsMapper.top150(param);
	}

	@Override
	public List<TopicWordsDTO> top10(TopicWordsDTO param) {
		// TODO Auto-generated method stub
		return wordsMapper.top10(param);
	}

	@Override
	public List<TopicWordsDTO> getChangeRate(TopicWordsDTO param) {
		// TODO Auto-generated method stub
		return wordsMapper.getChangeRate(param);
	}
	
	// 테스트용으로 미사용
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	// 테스트용으로 미사용
	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}
	// 테스트용으로 미사용
	@Override
	public int saveAll() {
		// TODO Auto-generated method stub
		return 0;
	}

}
