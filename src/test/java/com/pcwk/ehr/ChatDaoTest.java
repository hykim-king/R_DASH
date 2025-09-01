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
import org.springframework.test.context.web.WebAppConfiguration;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.mapper.ChatMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
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

		// FK 충돌 방지: USER_NO는 NULL 허용이라 테스트 기본값은 null로
		dto01 = new ChatDTO(null, null, "session-1001", "당신이 재난입니까?", "잘못된 질문이옵니다.", null);

		search = new SearchDTO();
		search.setPageNo(1);
		search.setPageSize(10);
	}

	@AfterEach
	void tearDown() {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}

	//@Disabled
	@Test
	void insertAndSelect() {
		mapper.deleteAll();

		int flag = mapper.insertChat(dto01);
		assertEquals(1, flag);
		log.debug("after insert, dto01.logNo={}", dto01.getLogNo());

		Long outVO = dto01.getLogNo();
		ChatDTO out;

		if (outVO != null) {
			out = mapper.selectChat(outVO);
			assertNotNull(out, "PK로 조회 실패");
		} else {
			// useGeneratedKeys 미지원 시 fallback 검색
			SearchDTO s = new SearchDTO();
			s.setPageNo(1);
			s.setPageSize(1);
			s.setSearchDiv("10"); // QUESTION
			s.setSearchWord(dto01.getQuestion());
			List<ChatDTO> list = mapper.chatList(s);
			assertTrue(list.size() > 0, "insert 후 조회 실패");
			out = list.get(0);
		}

		assertNotNull(out.getLogNo(), "outVO가 null입니다.");
		isSameChat(out, dto01);
	}

	//@Disabled
	@Test
	void update() {
		mapper.deleteAll();
		mapper.insertChat(dto01);

		ChatDTO dbData = fetchByQuestion(dto01.getQuestion());
		assertNotNull(dbData, "insert 데이터 조회 실패");

		dbData.setQuestion("재난 행동강령 알려줘~~~");
		dbData.setAnswer("나도 잘 모르겠어@@@@");

		int flag = mapper.updateChat(dbData);
		assertEquals(1, flag, "update 실패");

		ChatDTO updated = mapper.selectChat(dbData.getLogNo());
		assertNotNull(updated, "update 후 단건 조회 실패");
		assertEquals("재난 행동강령 알려줘~~~", updated.getQuestion());
		assertEquals("나도 잘 모르겠어@@@@", updated.getAnswer());
	}

	//@Disabled
	@Test
	void delete() {
		mapper.deleteAll();
		mapper.insertChat(dto01);

		ChatDTO inserted = fetchByQuestion(dto01.getQuestion());
		assertNotNull(inserted, "insert 데이터 조회 실패");

		int flag = mapper.deleteChat(inserted.getLogNo());
		assertEquals(1, flag, "delete 실패");

		List<ChatDTO> list = mapper.chatList(new SearchDTO());
		assertEquals(0, list.size(), "삭제 후에도 데이터 존재");
	}

	//@Disabled
	@Test
	void doRetrieveList() {
		mapper.deleteAll();

		for (int i = 0; i < 5; i++) {
			String sessionId = "session-" + i;
			ChatDTO dto = new ChatDTO(null, null, sessionId, "질문" + i, "답변" + i, null);
			int flag = mapper.insertChat(dto);
			assertEquals(1, flag, "insert 실패");
		}

		search.setSearchDiv("10"); // QUESTION
		search.setSearchWord("질문");

		List<ChatDTO> list = mapper.chatList(search);
		assertNotNull(list, "리스트 null");
		assertTrue(list.size() > 0, "검색된 데이터 없음");

		list.forEach(vo -> log.debug("vo={}", vo));
	}

	private ChatDTO fetchByQuestion(String question) {// 특정 질문(question) 내용으로 채팅 로그 목록을 검색해서, 가장 첫 번째 결과를 반환하는 도우미
														// 메서드입니다.
		SearchDTO s = new SearchDTO();// 검색/페이징 조건을 담는 DTO 객체를 새로 만듭니다.
		s.setPageNo(1);// 1페이지를 가져오고, 페이지 크기는 1건만 가져오도록 설정.
		s.setPageSize(1);// 검색 결과 중 첫 번째 데이터만 가져오게 됩니다.
		s.setSearchDiv("10"); // QUESTION searchDiv 값 "10"은 XML 매퍼에서 "QUESTION LIKE '%검색어%'"로 해석되도록 되어 있습니다.
		s.setSearchWord(question);// 검색어를 question 파라미터 값으로 지정.
		List<ChatDTO> list = mapper.chatList(s);
		return list.isEmpty() ? null : list.get(0);
	}

	private void isSameChat(ChatDTO actual, ChatDTO expected) {
		assertEquals(expected.getUserNo(), actual.getUserNo(), "userNo 불일치");
		assertEquals(expected.getSessionId(), actual.getSessionId(), "sessionId 불일치");
		assertEquals(expected.getQuestion(), actual.getQuestion(), "question 불일치");
		assertEquals(expected.getAnswer(), actual.getAnswer(), "answer 불일치");
	}

	//@Disabled
	@Test
	void beans() {
		assertNotNull(context);
		assertNotNull(mapper);
		assertNotNull(search);
		assertNotNull(dto01);

		log.debug("context: {}", context);
		log.debug("mapper: {}", mapper);
		log.debug("search: {}", search);
		log.debug("dto01: {}", dto01);
	}
}