package com.pcwk.ehr.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.mapper.BoardMapper;

public class BoardServiceImpl implements BoardService {
	
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	BoardMapper boardMapper;

	@Override
	public List<BoardDTO> doRetrieve(SearchDTO param) {
		return boardMapper.doRetrieve(param);
	}

	@Override
	public BoardDTO doSelectOne(BoardDTO param) {
		
		//로그인이 되어 있으며,
		//admin -> role == 1 일 경우만 조회수 오름.
		int flag = boardMapper.updateViews();
		log.debug("flag: {}",flag);
		
		return boardMapper.doSelectOne(param);
	}

	@Override
	public int doSave(BoardDTO param) {
		return boardMapper.doSave(param);
	}

	@Override
	public int doUpdate(BoardDTO param) {
		return boardMapper.doUpdate(param);
	}

	@Override
	public int doDelete(BoardDTO param) {
		return boardMapper.doDelete(param);
	}

}
