package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.ChatDTO;

@Mapper
public interface ChatMapper extends WorkDiv<ChatDTO> {

	int insertChat(ChatDTO chat);

	ChatDTO selectChat(@Param("logNo") Long logNo); // ✅ Integer → Long

	List<ChatDTO> chatList(SearchDTO search);

	int updateChat(ChatDTO chat);

	int deleteChat(@Param("logNo") Long logNo); // ✅ Integer → Long

	int deleteAll();
	
	 List<ChatDTO> findRecentBySession(@Param("sessionId") String sessionId,
             @Param("userNo") Integer userNo,
             @Param("limit") int limit);
	
	
}