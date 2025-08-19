package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.domain.TopicWordsDTO;

@Mapper
public interface TopicWordsMapper {
	List<TopicWordsDTO> top150(TopicWordsDTO param);
	
	List<TopicWordsDTO> top10(TopicWordsDTO param);
		
	List<TopicWordsDTO> getChangeRate(TopicWordsDTO param);
	
	void deleteAll();
	
	int saveAll();
	
	int getCount();
}
