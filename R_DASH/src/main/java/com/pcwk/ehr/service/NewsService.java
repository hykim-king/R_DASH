package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.NewsDTO;

/**
 * News & Topic Service layer
 * @author user
 *
 */
@Service
public interface NewsService {
	
	List<NewsDTO> searchByKeyword(NewsDTO param);
	
	int doUpdate(NewsDTO param);
	
	int doSave(NewsDTO param);
	
	
	
}
