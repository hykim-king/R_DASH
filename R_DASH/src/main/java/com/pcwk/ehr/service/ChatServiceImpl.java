package com.pcwk.ehr.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.mapper.ChatMapper;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	ChatMapper chatMapper;

	@Override
	public int insertChat(ChatDTO chat) {
		return chatMapper.insertChat(chat);
	}

	@Override
	public ChatDTO selectChat(Integer logNo) {
		return chatMapper.selectChat(logNo);
	}

	@Override
	public List<ChatDTO> chatList(SearchDTO search) {
		return chatMapper.chatList(search);
	}

	@Override
	public int updateChat(ChatDTO chat) {
		return chatMapper.updateChat(chat);
	}

	@Override
	public int deleteChat(Integer logNo) {
		return chatMapper.deleteChat(logNo);
	}

	@Override
	public int deleteAll() {
		return chatMapper.deleteAll();
	}
}