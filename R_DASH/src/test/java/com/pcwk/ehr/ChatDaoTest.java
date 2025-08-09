package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.mapper.ChatMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml" })
class ChatDaoTest {

	final Logger log = LogManager.getLogger(getClass());

	@Autowired
	ApplicationContext context;

	@Autowired
	ChatMapper mapper;

	ChatDTO dto01;
	SearchDTO search;

	@BeforeEach
	void setUp() {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");

		dto01 = new ChatDTO(null, 2122, "1001", "당신이 재난입니까?", "잘못된 질문이옵니다.", null);
		search = new SearchDTO();
	}

	@AfterEach
	void tearDown() {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}

	// @Disabled
	@Test
	void doInsertAndSelect() {
		mapper.deleteAll();

		int flag = mapper.insertChat(dto01);
		assertEquals(1, flag, "insert 실패");

		// SELECT로 방금 넣은 데이터 조회
		search.setPageNo(1);
		search.setPageSize(1);
		search.setSearchDiv("10");
		search.setSearchWord(dto01.getQuestion());

		List<ChatDTO> resultList = mapper.chatList(search);
		assertTrue(resultList.size() > 0, "insert 후 조회 실패");

		ChatDTO outVO = resultList.get(0);
		assertNotNull(outVO, "조회 결과 없음");
		assertNotNull(outVO.getLogNo(), "logNo가 null입니다.");

		isSameChat(outVO, dto01);
	}

	// @Disabled
	@Test
	void doUpdate() {
		mapper.deleteAll();
		mapper.insertChat(dto01);

		// insert 후 조회
		search.setPageNo(1);
		search.setPageSize(1);
		search.setSearchDiv("10");
		search.setSearchWord(dto01.getQuestion());
		ChatDTO dbData = mapper.chatList(search).get(0);
		assertNotNull(dbData);

		dbData.setQuestion("재난 행동강령 알려줘~~~");
		dbData.setAnswer("나도 잘 모르겠어@@@@");

		int flag = mapper.updateChat(dbData);
		assertEquals(1, flag, "update 실패");

		ChatDTO updated = mapper.selectChat(dbData.getLogNo());
		assertEquals("재난 행동강령 알려줘~~~", updated.getQuestion());
		assertEquals("나도 잘 모르겠어@@@@", updated.getAnswer());
	}

	// @Disabled
	@Test
	void doDelete() {
		mapper.deleteAll();
		mapper.insertChat(dto01);

		// insert 후 조회
		search.setPageNo(1);
		search.setPageSize(1);
		search.setSearchDiv("10");
		search.setSearchWord(dto01.getQuestion());
		ChatDTO inserted = mapper.chatList(search).get(0);

		int flag = mapper.deleteChat(inserted.getLogNo());
		assertEquals(1, flag, "delete 실패");

		List<ChatDTO> list = mapper.chatList(new SearchDTO());
		assertEquals(0, list.size(), "삭제 후에도 데이터 존재");
	}

	// @Disabled
	@Test
	void doRetrieve() {
		mapper.deleteAll();

		for (int i = 0; i < 5; i++) {
			String sessionId = "session-" + i;
			ChatDTO dto = new ChatDTO(null, 2122, sessionId, "질문" + i, "답변" + i, null);
			int flag = mapper.insertChat(dto);
			assertEquals(1, flag, "insert 실패");
		}

		search.setPageNo(1);
		search.setPageSize(10);
		search.setSearchDiv("10");
		search.setSearchWord("질문");

		List<ChatDTO> list = mapper.chatList(search);
		assertNotNull(list);
		assertTrue(list.size() > 0, "검색된 데이터 없음");

		list.forEach(vo -> log.debug("vo={}", vo));
	}

	private void isSameChat(ChatDTO actual, ChatDTO expected) {
		assertEquals(expected.getUserNo(), actual.getUserNo(), "userNo 불일치");
		assertEquals(expected.getSessionId(), actual.getSessionId(), "sessionId 불일치");
		assertEquals(expected.getQuestion(), actual.getQuestion(), "question 불일치");
		assertEquals(expected.getAnswer(), actual.getAnswer(), "answer 불일치");
	}

	// @Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");

		assertNotNull(context);
		assertNotNull(mapper);
		assertNotNull(search);
		assertNotNull(dto01);

		log.debug("1. context: {}", context);
		log.debug("2. mapper: {}", mapper);
		log.debug("3. search: {}", search);
		log.debug("4. dto01: {}", dto01);
	}
}