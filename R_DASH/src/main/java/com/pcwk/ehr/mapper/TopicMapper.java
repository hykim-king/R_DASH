package com.pcwk.ehr.mapper;

import java.util.List;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.TopicDTO;

public interface TopicMapper extends WorkDiv<TopicDTO> {
	
	// 한건 조회 0 
	// 리스트 조회 (주제,내용,건수,오늘 날짜)
	// 저장
	// 수정 0
	// 삭제 0 
	
	/**
	 * 오늘의 토픽 조회
	 * @param param
	 * @return
	 */
	List<TopicDTO> getTodayTopicsList(TopicDTO param);
	
	/**
	 * test용 query
	 * @return
	 */
	public int getCount();
	
	public void deleteAll();
	
	public int saveAll();
}
