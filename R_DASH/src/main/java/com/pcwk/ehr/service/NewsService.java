package com.pcwk.ehr.service;

import java.util.List;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.NewsDTO;
import com.pcwk.ehr.domain.TopicDTO;

/**
 * News & Topic Service layer
 * @author user
 *
 */
public interface NewsService {	
	/**
	 * News Table
	 * @param NewsDTO param
	 * @return
	 */
	List<NewsDTO> searchByKeyword(NewsDTO param);
	
	int doUpdate(NewsDTO param);
	
	int doSave(NewsDTO param);
	
	List<NewsDTO> doRetrieve(SearchDTO param);
	
	int newsDelete(NewsDTO param);
	
	List<NewsDTO>newsMainList(NewsDTO param);
	
	/**
	 * Topic Table
	 * @param param
	 * @return
	 */
	List<TopicDTO> getTodayTopicsList(TopicDTO param);
	
	TopicDTO doSelectOne(TopicDTO param);
	
	int doSave(TopicDTO param);
	
	int doUpdate(TopicDTO param);
	
	int doDelete(TopicDTO param);	
}
