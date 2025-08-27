package com.pcwk.ehr.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.domain.ChatSessionSummary;
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

	@Override
	public List<ChatDTO> findRecentBySession(String sessionId, Integer userNo, int limit) {
		return chatMapper.findRecentBySession(sessionId, userNo, limit);
	}

	@Override
	public List<ChatSessionSummary> listSessions(Integer userNo, int limit) {
		return chatMapper.listSessions(userNo, Math.max(1, Math.min(limit, 200)));
	}

	@Override
	public List<ChatDTO> listMessagesBySession(String sessionId, Integer userNo, Long beforeLogNo, int limit) {
		return chatMapper.listMessagesBySession(sessionId, userNo, beforeLogNo, Math.max(1, Math.min(limit, 200)));
	}

	@Override
	public boolean existsSessionForUser(String sessionId, Integer userNo) {
		if (sessionId == null || userNo == null)
			return false;
		return chatMapper.countBySessionAndUser(sessionId, userNo) > 0;
	}

	@Override
	public boolean isGuestSession(String sessionId) {
		if (sessionId == null)
			return false;
		return chatMapper.countGuestBySession(sessionId) > 0;
	}

	@Override
	public boolean hasAnyUserLogs(String sessionId) {
		return chatMapper.countUserBySession(sessionId) > 0;
	}
}