package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
// DAO/Mapper 테스트이므로 root-context.xml만 로드
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

		dto01 = new ChatDTO(null, // logNo (PK, 자동 생성)
				1, // userNo
				1001, // sessionId
				"당신이 재난입니까?", // question
				"잘못된 질문이옵니다.", // answer
				null // regDt (DB SYSDATE)
		);

		search = new SearchDTO();
	}

	@AfterEach
	void tearDown() {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}

	@Disabled
	@Test
	void doInsertAndSelect() {
		mapper.deleteAll();

		int flag = mapper.insertChat(dto01);
		assertEquals(1, flag, "insertChat 실패");

		ChatDTO outVO = mapper.selectChat(dto01.getLogNo());
		assertNotNull(outVO, "조회 결과 없음");

		isSameChat(outVO, dto01);
	}

	@Disabled
	@Test
	void doUpdate() {
		mapper.deleteAll();
		mapper.insertChat(dto01);

		ChatDTO dbData = mapper.selectChat(dto01.getLogNo());
		assertNotNull(dbData);

		dbData.setQuestion("수정된 질문");
		dbData.setAnswer("수정된 답변");

		int flag = mapper.updateChat(dbData);
		assertEquals(1, flag, "updateChat 실패");

		ChatDTO updated = mapper.selectChat(dbData.getLogNo());
		assertEquals("수정된 질문", updated.getQuestion());
		assertEquals("수정된 답변", updated.getAnswer());
	}

	@Disabled
	@Test
	void doDelete() {
		mapper.deleteAll();
		mapper.insertChat(dto01);

		int flag = mapper.deleteChat(dto01.getLogNo());
		assertEquals(1, flag, "deleteChat 실패");

		assertEquals(0, mapper.chatList(search).size(), "삭제 후 데이터가 남아있음");
	}

	//@Disabled
	@Test
	void doRetrieve() {
		mapper.deleteAll();

		for (int i = 0; i < 5; i++) {
			mapper.insertChat(new ChatDTO(null, 1, 1000 + i, "질문" + i, "답변" + i, null));
		}

		search.setPageNo(1);
		search.setPageSize(10);
		search.setSearchDiv("10"); // question 검색
		search.setSearchWord("질문");

		List<ChatDTO> list = mapper.chatList(search);
		assertNotNull(list);
		assertTrue(list.size() > 0);

		list.forEach(vo -> log.debug("vo={}", vo));
	}

	private void isSameChat(ChatDTO actual, ChatDTO expected) {
		assertEquals(expected.getUserNo(), actual.getUserNo(), "userNo 불일치");
		assertEquals(expected.getSessionId(), actual.getSessionId(), "sessionId 불일치");
		assertEquals(expected.getQuestion(), actual.getQuestion(), "question 불일치");
		assertEquals(expected.getAnswer(), actual.getAnswer(), "answer 불일치");
	}
	@Disabled
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