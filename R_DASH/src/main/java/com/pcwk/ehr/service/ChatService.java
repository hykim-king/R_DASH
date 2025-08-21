package com.pcwk.ehr.service;

import java.util.List;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;

public interface ChatService {
	/**
	 * 채팅 로그 등록
	 * 
	 * @param chat ChatDTO
	 * @return 등록 건수
	 */
	int insertChat(ChatDTO chat);

	/**
	 * 채팅 로그 단건 조회
	 * 
	 * @param logNo 기록 번호
	 * @return ChatDTO
	 */
	ChatDTO selectChat(Long logNo); // ✅ Integer → Long

	/**
	 * 채팅 로그 목록 조회 (페이징 + 검색)
	 * 
	 * @param search SearchDTO
	 * @return List<ChatDTO>
	 */
	List<ChatDTO> chatList(SearchDTO search);

	/**
	 * 채팅 로그 수정
	 * 
	 * @param chat ChatDTO
	 * @return 수정 건수
	 */
	int updateChat(ChatDTO chat);

	/**
	 * 채팅 로그 삭제
	 * 
	 * @param logNo 기록 번호
	 * @return 삭제 건수
	 */
	int deleteChat(Long logNo); // ✅ Integer → Long

	/**
	 * 전체 채팅 로그 삭제 (테스트용)
	 * 
	 * @return 삭제 건수
	 */
	int deleteAll();

	List<ChatDTO> findRecentBySession(String sessionId, Integer userNo, int limit);

}