package com.pcwk.ehr.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.domain.NewsDTO;
import com.pcwk.ehr.domain.TopicDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.NewsMapper;
import com.pcwk.ehr.mapper.TopicMapper;

@Service
public class NewsServiceImpl implements NewsService {
	
	@Autowired
	NewsMapper newsMapper;
	
	@Autowired
	TopicMapper topicMapper;
	
	UserDTO userDTO;
	
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	HttpSession session;

	@Override
	public List<NewsDTO> searchByKeyword(NewsDTO param) {
		return newsMapper.searchByKeyword(param);
	}

	@Override
	public int doUpdate(NewsDTO param) {
		return newsMapper.doUpdate(param);
	}

	@Override
	public int doSave(NewsDTO param) {
		return newsMapper.doSave(param);
	}

	@Override
	public List<TopicDTO> getTodayTopicsList(TopicDTO param) {
		return topicMapper.getTodayTopicsList(param);
	}

	@Override
	public TopicDTO doSelectOne(TopicDTO param) {
		return topicMapper.doSelectOne(param);
	}

	@Override
	public int doSave(TopicDTO param) {
		//컨트롤러에서 세션 설정되면 변경하기.
		UserDTO user = (UserDTO) session.getAttribute("user"); 
		if(user==null) {
			throw new NullPointerException("로그인 필요");
		}else if(user.getRole() != 1) {
			throw new RuntimeException("관리자 권한 필요");
		}
		param.setRegId(user.getNickname());
		return topicMapper.doSave(param);
	}

	@Override
	public int doUpdate(TopicDTO param) {
		UserDTO user = (UserDTO) session.getAttribute("user"); 

		if(user==null) {
			throw new NullPointerException("로그인 필요");
		}else if(user.getRole() != 1) {
			throw new RuntimeException("관리자 권한 필요");
		}
		param.setModId(user.getNickname());
		return topicMapper.doUpdate(param);
	}

	@Override
	public int doDelete(TopicDTO param) {
		return topicMapper.doDelete(param);
	}

}
