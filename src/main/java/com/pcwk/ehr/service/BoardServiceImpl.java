package com.pcwk.ehr.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {
	
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	HttpSession session;
	
	@Autowired
	BoardMapper boardMapper;

	@Override
	public List<BoardDTO> doRetrieve(SearchDTO param) {
		return boardMapper.doRetrieve(param);
	}
	/**
	 * 관리자가 아닌 사용자일 때만 조회수 증가
	 */
	@Override
	public BoardDTO doSelectOne(BoardDTO param) {
		//admin -> role == 1 일 경우만 조회수 오름.
		UserDTO user = (UserDTO) session.getAttribute("user"); 
		
		if(user == null) {
			boardMapper.updateViews(param);
		}else if(user.getRole() != 1) {
			boardMapper.updateViews(param);
		}
		
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
