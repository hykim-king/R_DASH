package com.pcwk.ehr.mapper;

import java.util.List;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.NewsDTO;

public interface NewsMapper extends WorkDiv<NewsDTO> {
	//전체 저장(Test 용)
	public int saveAll();
	//전체 삭제(Test 용)
	public void deleteAll();
	//전체 개수 조회(Test 용)
	public int getCount();
	//키 워드로 조회
	List<NewsDTO> searchByKeyword(NewsDTO param);
	
}
