package com.pcwk.ehr.mapper;

import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.NewsDTO;

public interface NewsMapper extends WorkDiv<NewsDTO> {
	//전체 저장(Test 용)
	public int saveAll();
	//전체 삭제(Test 용)
	public void deleteAll();
	//전체 개수 조회(Test 용)
	public int getCount();
}
