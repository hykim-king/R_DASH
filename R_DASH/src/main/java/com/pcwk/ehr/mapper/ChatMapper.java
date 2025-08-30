package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.domain.ChatSessionSummary;

@Mapper
public interface ChatMapper extends WorkDiv<ChatDTO> {

	int insertChat(ChatDTO chat);

	ChatDTO selectChat(@Param("logNo") Long logNo); // ✅ Integer → Long

	List<ChatDTO> chatList(SearchDTO search);

	int updateChat(ChatDTO chat);

	int deleteChat(@Param("logNo") Long logNo); // ✅ Integer → Long

	int deleteAll();

	boolean existsSessionForUser(String sessionId, Integer userNo);

	boolean isGuestSession(String sessionId);

	List<ChatDTO> findRecentBySession(@Param("sessionId") String sessionId, @Param("userNo") Integer userNo,
			@Param("limit") int limit);

	List<ChatSessionSummary> listSessions(@Param("userNo") Integer userNo, @Param("limit") int limit);

	List<ChatDTO> listMessagesBySession(@Param("sessionId") String sessionId, @Param("userNo") Integer userNo,
			@Param("beforeLogNo") Long beforeLogNo, @Param("limit") int limit);

	int countBySessionAndUser(@Param("sessionId") String sessionId, @Param("userNo") Integer userNo);

	int countGuestBySession(@Param("sessionId") String sessionId);
	
	int countUserBySession(@Param("sessionId") String sessionId); // ★ 추가
	
	/** 이 세션이 이 유저 소유인지(로그/세션 메타 기준) */
    int countSessionOwnedByUser(@Param("sessionId") String sessionId,
                                @Param("userNo") Integer userNo);

    /** 세션의 메시지 삭제(과거 null/혼합 데이터 대비: NVL(USER_NO, :userNo) = :userNo) */
    int deleteMessagesBySessionAndUser(@Param("sessionId") String sessionId,
                                       @Param("userNo") Integer userNo);

    /** 세션 메타 삭제(세션 테이블이 있을 때) — 없으면 XML에서 스텁(0 리턴) 처리 가능 */
    int deleteSessionByUser(@Param("sessionId") String sessionId,
                            @Param("userNo") Integer userNo);
    
    int deleteMessagesBySession(@Param("sessionId") String sessionId);

}