package com.pcwk.ehr.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.mapper.ChatMapper;

@Service("chatService")
@Transactional(readOnly = true) // 기본은 조회 전용
public class ChatServiceImpl implements ChatService {

	Logger log = LogManager.getLogger(ChatServiceImpl.class);

	private final ChatMapper chatMapper;

	public ChatServiceImpl(ChatMapper chatMapper) {
		this.chatMapper = chatMapper;
	}

	@Override
	@Transactional // 쓰기 작업만 트랜잭션
	public int insertChat(ChatDTO chat) {
		log.debug("insertChat 호출:{}", chat);
		return chatMapper.insertChat(chat);
	}

	@Override
	public ChatDTO selectChat(Long logNo) {
		return chatMapper.selectChat(logNo);
	}

	@Override
	public List<ChatDTO> chatList(SearchDTO search) {
		return chatMapper.chatList(search);
	}

	@Override
	@Transactional
	public int updateChat(ChatDTO chat) {
		return chatMapper.updateChat(chat);
	}

	@Override
	@Transactional
	public int deleteChat(Long logNo) {
		return chatMapper.deleteChat(logNo);
	}

	@Override
	@Transactional
	public int deleteAll() {
		return chatMapper.deleteAll();
	}
}