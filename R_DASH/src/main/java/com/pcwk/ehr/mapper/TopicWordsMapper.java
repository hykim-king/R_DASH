package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.domain.TopicWordsDTO;

@Mapper
public interface TopicWordsMapper {
	List<TopicWordsDTO> top100();
	
	List<TopicWordsDTO> top10();
		
	List<TopicWordsDTO> getChangeRate();
	
	void deleteAll();
	
	int saveAll();
	
	int getCount();
}
