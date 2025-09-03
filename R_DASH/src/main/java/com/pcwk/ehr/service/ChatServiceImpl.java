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

@Service("ChatService")
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
	    int c = chatMapper.countGuestBySession(sessionId);
	    log.debug("[debug] guestCnt(sid={})={}", sessionId, c);
	    return c > 0;
	}

	@Override
	public boolean hasAnyUserLogs(String sessionId) {
	    int c = chatMapper.countUserBySession(sessionId);
	    log.debug("[debug] userCnt(sid={})={}", sessionId, c);
	    return c > 0;
	}

	@Transactional
	@Override
	public boolean deleteSessionForUser(String sessionId, int userNo) {
		// 1) 소유권 판정: CHAT_SESSION → 실패/없으면 CHAT_LOG로 폴백
		boolean owned = false;
		try {
			int metaCnt = chatMapper.countSessionOwnedByUser(sessionId, userNo);
			owned = metaCnt > 0;
			log.debug("[delete] session meta owned? {} (sid={}, userNo={})", owned, sessionId, userNo);
		} catch (Exception e) {
			log.warn("[delete] countSessionOwnedByUser failed, fallback to logs: {}", e.toString());
		}
		if (!owned) {
			int logCnt = chatMapper.countBySessionAndUser(sessionId, userNo);
			owned = logCnt > 0;
			log.debug("[delete] log-based owned? {} (sid={}, userNo={}, logCnt={})", owned, sessionId, userNo, logCnt);
		}
		if (!owned)
			return false; // 403

		// 2) 메시지 먼저 일괄 삭제(혼합 데이터 대비, session 기준)
		int msgDel = 0;
		try {
			msgDel = chatMapper.deleteMessagesBySession(sessionId);
		} catch (Exception e) {
			log.error("[delete] deleteMessagesBySession error (sid={}): {}", sessionId, e.toString());
		}

		// 3) 세션 메타 삭제(테이블/행이 없으면 예외 무시)
		int sessDel = 0;
		try {
			sessDel = chatMapper.deleteSessionByUser(sessionId, userNo);
		} catch (Exception e) {
			log.debug("[delete] deleteSessionByUser ignore (no meta table/row). sid={}, userNo={}, err={}", sessionId,
					userNo, e.toString());
		}

		log.debug("[delete] done. sid={}, userNo={}, msgDel={}, sessDel={}", sessionId, userNo, msgDel, sessDel);
		return true; // 204
	}

}