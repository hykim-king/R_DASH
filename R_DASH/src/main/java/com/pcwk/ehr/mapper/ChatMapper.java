package com.pcwk.ehr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.cmn.WorkDiv;
import com.pcwk.ehr.domain.ChatDTO;

@Mapper
public interface ChatMapper extends WorkDiv<ChatDTO> {

	int insertChat(ChatDTO chat);

	ChatDTO selectChat(Long logNo); // ✅ Integer → Long

	List<ChatDTO> chatList(SearchDTO search);

	int updateChat(ChatDTO chat);

	int deleteChat(Long logNo); // ✅ Integer → Long

	int deleteAll();
}