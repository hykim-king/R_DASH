package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;

public interface BoardService {
	List<BoardDTO> doRetrieve(SearchDTO param);
	
	BoardDTO doSelectOne(BoardDTO param);
	
	int doSave(BoardDTO param);
	
	int doUpdate(BoardDTO param);
	
	int doDelete(BoardDTO param);
	
	List<BoardDTO> boardMainList();

}
