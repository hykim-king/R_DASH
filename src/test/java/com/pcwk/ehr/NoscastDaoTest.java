package com.pcwk.ehr;

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

import com.pcwk.ehr.domain.NowcastDTO;
import com.pcwk.ehr.mapper.NowcastMapper;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context-test.xml" })
public class NoscastDaoTest {

	Logger log = LogManager.getLogger(getClass());

	@Autowired
	NowcastMapper mapper;

	@Autowired
	ApplicationContext context;

	@BeforeEach
	public void setUp() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ setUp()            │");
		log.debug("└────────────────────┘");

	}

	@AfterEach
	public void tearDown() throws Exception {
		log.debug("┌────────────────────┐");
		log.debug("│ tearDown()         │");
		log.debug("└────────────────────┘");
	}

//	전체 목록 조회
	@Disabled
	@Test
	public void selectAllNowTest() {
		List<NowcastDTO> list = mapper.selectAll();
		log.debug("┌────────────────────────┐");
		log.debug("│ selectAllNowTest()     │");
		log.debug("└────────────────────────┘");

		assertNotNull(list);
		assertTrue(list.size() >= 0);
		list.forEach(log::debug);
		log.debug("전체 목록 건수: {}", list.size());
	}

//	단건 조회
//	@Disabled
	@Test
	public void findByIdNowTest() {
		String testsidoNm = "경상남도";
		NowcastDTO dto = mapper.selectSidoNm(testsidoNm);
		log.debug("┌───────────────────────────┐");
		log.debug("│ findByIdNowTest()         │");
		log.debug("└───────────────────────────┘");

		assertNotNull(dto);
		log.debug("단건 조회: {}", dto);

	}
}
