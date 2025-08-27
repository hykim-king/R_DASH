package com.pcwk.ehr.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.NewsDTO;
import com.pcwk.ehr.mapper.NewsMapper;

@Controller
public class MainController {

	private static final Logger log = LogManager.getLogger(MainController.class);

	@Autowired
	private NewsMapper newsMapper;

	public MainController() {
		log.debug("┌─────────────────────────────────────┐");
		log.debug("│ MainController() 생성자 호출        │");
		log.debug("└─────────────────────────────────────┘");
	}

	/**
	 * 메인 화면 출력 URL: http://localhost:8080/ehr/home
	 */
	@GetMapping("/home")
	public String home(Model model) {
		log.debug("┌─────────────────────┐");
		log.debug("│ home() 메서드 실행  │");
		log.debug("└─────────────────────┘");

		// 오늘자(reg_dt) 최신 9건
		SearchDTO s = new SearchDTO();
		s.setPageNo(1);
		s.setPageSize(9);
		s.setSearchDiv("");
		s.setSearchWord("");

		List<NewsDTO> homeNews = newsMapper.doRetrieve(s);

		String homeNewsJson = "[]";
		try {
			homeNewsJson = new ObjectMapper().writeValueAsString(homeNews);
			// ★ 안전 보강: </script> 방지
			homeNewsJson = homeNewsJson.replace("</", "<\\/");
		} catch (JsonProcessingException e) {
			log.error("homeNews JSON 직렬화 실패", e); // 실패해도 빈 배열로 내려가게
		}

		model.addAttribute("homeNews", homeNews);
		model.addAttribute("homeNewsJson", homeNewsJson);
		model.addAttribute("msg", "저희 재난 알림 사이트를 방문해주셔서 감사합니다.");
		return "main/main";
	}

	/*
	 * // ✅ 오늘자(reg_dt) 최신 9건
	 * 
	 * @GetMapping(value = "/api/home-news", produces =
	 * "application/json; charset=UTF-8")
	 * 
	 * @ResponseBody public List<NewsDTO> homeNews() { SearchDTO s = new
	 * SearchDTO(); s.setPageNo(1); s.setPageSize(9); // 3장 × 3개 s.setSearchDiv("");
	 * // (뉴스 doRetrieve는 reg_dt=SYSDATE 조건이 XML에 이미 있음) s.setSearchWord(""); return
	 * newsMapper.doRetrieve(s); }
	 */

	/**
	 * 검색 처리 URL: http://localhost:8080/ehr/search?keyword=검색어
	 */
	@GetMapping("/search")
	public String search(@RequestParam("keyword") String keyword, Model model) {
		log.debug("검색 요청 keyword: {}", keyword);

		// 여기서 DB 검색 또는 API 연동 로직 수행
		// 예: 검색어를 그대로 결과로 반환
		model.addAttribute("keyword", keyword);
		model.addAttribute("results", new ArrayList<>()); // 결과 리스트 넣기

		return "searchResults"; // /WEB-INF/views/searchResults.jsp
	}
}