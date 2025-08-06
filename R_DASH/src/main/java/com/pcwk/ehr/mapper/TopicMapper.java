package com.pcwk.ehr.mapper;

import java.util.List;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.TopicDTO;

public interface TopicMapper extends WorkDiv<TopicDTO> {
	
	/**
	 * 오늘의 토픽 조회
	 * @param param
	 * @return
	 */
	List<TopicDTO> getTodayTopics(TopicDTO param);
	
	/**
	 * test용 query
	 * @return
	 */
	public int getCount();
	
	public void deleteAll();
	
	public int saveAll();
}
