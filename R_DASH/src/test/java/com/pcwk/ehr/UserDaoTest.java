package com.pcwk.ehr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.UserMapper;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
class UserDaoTest {
	Logger log = LogManager.getLogger(getClass());

	@Autowired
	ApplicationContext context;

	@Autowired
	UserMapper mapper;

	UserDTO dto01;
	UserDTO dto02;
	UserDTO dto03;

	SearchDTO search;

	@BeforeEach
	void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");

		dto01 = new UserDTO(1, "1234", "이상무1", "부리부리1", "이미지", "pcwkkk1@gmail.com", "010-1234-5678", 12345, "z", "z", 1,
				"z");
		dto02 = new UserDTO(1, "1234", "이상무2", "부리부리2", "이미지", "pcwkkk2@gmail.com", "010-1234-5678", 12345, "z", "z", 1,
				"z");
		dto03 = new UserDTO(1, "1234", "이상무3", "부리부리3", "이미지", "pcwkkk3@gmail.com", "010-1234-5678", 12345, "z", "z", 1,
				"z");

		search = new SearchDTO();
	}

	@AfterEach
	void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}
	@Test
	void findPw() {
		// 매번 동일한 결과가 도출 되도록 작성
		// 1. 전체삭제
		// 2. 단건등록
		// 3. 단건 조회
		// 4. 비밀번호 확인
		
		// 1.
		mapper.deleteAll();
		
		// 2.
		int flag = mapper.insertUser(dto01);
		assertEquals(1, flag);
		int count = mapper.getCount();
		assertEquals(1, count);
		
		// 3.
		int userNo = mapper.getUserNo(dto01.getEmail());
		UserDTO outVO = mapper.selectUser(userNo);
		assertNotNull(outVO);
		
		String pw = mapper.findPw(outVO.getEmail());
		assertEquals("1234", pw);
		
	}
	
	@Disabled
	@Test
	void updateRole() {
		// 매번 동일한 결과가 도출 되도록 작성
		// 1. 전체삭제
		// 2. 단건등록
		// 3. 단건 조회
		// 4. 권한 변경
		
		// 1.
		mapper.deleteAll();
		
		// 2.
		int flag = mapper.insertUser(dto01);
		assertEquals(1, flag);
		int count = mapper.getCount();
		assertEquals(1, count);
		
		// 3.
		int userNo = mapper.getUserNo(dto01.getEmail());
		UserDTO outVO = mapper.selectUser(userNo);
		assertNotNull(outVO);
		assertEquals(2, outVO.getRole());
		
		dto01.setUserNo(userNo);
		dto01.setRole(1);
		mapper.updateRole(dto01);
		outVO = mapper.selectUser(userNo);
		assertEquals(1, outVO.getRole());
	}
	
	@Disabled
	@Test
	void Retrieve() {
		// 매번 동일한 결과가 도출 되도록 작성
		// 1. 전체삭제
		// 2. 다건등록
		// 3. 페이징 조회
		
		// 1.
		mapper.deleteAll();
		
		// 2.
		int flag = mapper.insertUser(dto01);
		assertEquals(1, flag);
		int count = mapper.getCount();
		assertEquals(1, count);
		
		
		flag = mapper.insertUser(dto02);
		assertEquals(1, flag);
		count = mapper.getCount();
		assertEquals(2, count);
		
		flag = mapper.insertUser(dto03);
		assertEquals(1, flag);
		count = mapper.getCount();
		assertEquals(3, count);
		
		mapper.saveAll();
		count = mapper.getCount();
		assertEquals(103, count);
		
		// 3.
		search.setPageNo(1);
		search.setPageSize(10);
		List<UserDTO> list = mapper.userList(search);
		assertEquals(10, list.size());
		for(UserDTO dto : list) {
			log.debug("dto:{}",dto);
		}
		
		// 3. email 조회
		search.setSearchDiv("10");
		search.setSearchWord("wk");
		list = mapper.userList(search);
		assertEquals(10, list.size());
		for(UserDTO dto : list) {
			log.debug("dto:{}",dto);
		}
		
		// 3. 이름 조회
		search.setSearchDiv("20");
		search.setSearchWord("이상무3");
		list = mapper.userList(search);
		assertEquals(10, list.size());
		for(UserDTO dto : list) {
			log.debug("dto:{}",dto);
		}
		
		// 3. 관리자만 조회
//		search.setPageNo(1);
//		search.setPageSize(10);
//		search.setSearchDiv("30");
//		List<UserDTO> list = mapper.userList(search);
//		assertEquals(3, list.size());
//		for(UserDTO dto : list) {
//			log.debug("dto:{}",dto);
//		}
		
		
		
	}
	
	
	@Disabled
	@Test
	void updateUser() {
		// 매번 동일한 결과가 도출 되도록 작성
		// 1. 전체삭제
		// 2. 단건등록
		// 2.1 전체건수 조회
		// 3. 단건조회
		// 4. 이메일 check
		// 5. 회원정보수정
		// 6. 비밀번호 확인
		
		// 1.
		mapper.deleteAll();
		
		// 2.
		int flag = mapper.insertUser(dto01);
		assertEquals(1, flag);
		//2.1
		int count = mapper.getCount();
		assertEquals(1, count);
		
		// 3.
		int userNo = mapper.getUserNo(dto01.getEmail());
		UserDTO outVO = mapper.selectUser(userNo);
		assertNotNull(outVO);
		log.debug("outVO:{}",outVO);
		
		// 4.
		count = mapper.checkEmail(outVO.getEmail());
		assertEquals(1, count);
		
		// 5.
		outVO.setPassword("456789");
		mapper.updateUser(outVO);
		assertNotEquals("1234", outVO.getPassword());		
		
		// 6.
		count = mapper.checkPw(outVO);
		assertEquals(1, count);
		
	}
	
	@Disabled
	@Test
	void deleteUser() {
		// 매번 동일한 결과가 도출 되도록 작성
		// 1. 전체삭제
		// 2. 단건등록
		// 2.1 전체건수 조회
		// 3. 단건조회
		// 4. 단건삭제

		// 1.
		mapper.deleteAll();

		// 2.
		int flag = mapper.insertUser(dto01);
		assertEquals(1, flag);
		// 2.1
		int count = mapper.getCount();
		assertEquals(1, count);
		
		// 3.
		int userNo = mapper.getUserNo(dto01.getEmail());
		UserDTO outVO01 = mapper.selectUser(userNo);
		assertNotNull(outVO01);
		log.debug("outVO01:{}", outVO01);
		
		// 4.
		mapper.deleteUser(userNo);
		
		// 5.
		count = mapper.getCount();
		assertEquals(0, count);
			
	}

	@Disabled
	@Test
	void addAndGet() {
		// 매번 동일한 결과가 도출 되도록 작성
		// 1. 전체삭제
		// 2. 단건등록
		// 2.1 전체건수 조회
		// 3. 단건조회
		// 4. 전체 조회

		// 1.
		mapper.deleteAll();

		// 2.
		int flag = mapper.insertUser(dto01);
		assertEquals(1, flag);
		// 2.1
		int count = mapper.getCount();
		assertEquals(1, count);

		flag = mapper.insertUser(dto02);
		assertEquals(1, flag);
		count = mapper.getCount();
		assertEquals(2, count);

		flag = mapper.insertUser(dto03);
		assertEquals(1, flag);
		count = mapper.getCount();
		assertEquals(3, count);

		// 3.
		int userNo = mapper.getUserNo(dto01.getEmail());
		UserDTO outVO01 = mapper.selectUser(userNo);
		assertNotNull(outVO01);
		log.debug("outVO01:{}", outVO01);

		userNo = mapper.getUserNo(dto02.getEmail());
		UserDTO outVO02 = mapper.selectUser(userNo);
		assertNotNull(outVO02);
		log.debug("outVO02:{}", outVO02);

		userNo = mapper.getUserNo(dto03.getEmail());
		UserDTO outVO03 = mapper.selectUser(userNo);
		assertNotNull(outVO03);
		log.debug("outVO03:{}", outVO03);
		
		// 4.
		List<UserDTO> list = mapper.getAll();
		for(UserDTO dto : list) {
			assertNotNull(dto);
			log.debug("dto:{}",dto);
		}
	}

	@Disabled
	@Test
	void beans() {
		log.debug("┌────────────────────┐");
		log.debug("│ beans()            │");
		log.debug("└────────────────────┘");

		assertNotNull(context);
		assertNotNull(mapper);

		log.debug(context);
		log.debug(mapper);
	}

}
