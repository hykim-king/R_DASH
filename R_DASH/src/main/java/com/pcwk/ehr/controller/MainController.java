package com.pcwk.ehr.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.NewsDTO;
import com.pcwk.ehr.domain.UserDTO;
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
	public String home(Model model,
			@SessionAttribute(value = "loginUser", required = false) com.pcwk.ehr.domain.UserDTO loginUser,
			HttpServletResponse response) {
		log.debug("┌─────────────────────┐");
		log.debug("│ home() 메서드 실행  │");
		log.debug("└─────────────────────┘");

		// 캐시 방지(로그인 번호 비어 찍히는 문제 예방)
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
		response.setHeader("Pragma", "no-cache");

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
			homeNewsJson = homeNewsJson.replace("</", "<\\/");
		} catch (JsonProcessingException e) {
			log.error("homeNews JSON 직렬화 실패", e);
		}

		// ★ 로그인 사용자 번호 바인딩
		if (loginUser != null) {
			model.addAttribute("loginUserNo", loginUser.getUserNo());
			log.debug("loginUserNo in model: {}", loginUser.getUserNo());
		} else {
			model.addAttribute("loginUserNo", null);
			log.debug("loginUserNo in model: null");
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
	public String search(@RequestParam(value = "keyword", required = false) String keyword,
			@SessionAttribute(value = "loginUser", required = false) UserDTO loginUser, Model model) {
		log.debug("검색 요청 keyword: {}", keyword);

		if (loginUser != null)
			model.addAttribute("loginUserNo", loginUser.getUserNo());

		if (keyword == null || keyword.trim().isEmpty()) {
			return "redirect:/home";
		}

		model.addAttribute("keyword", keyword.trim());
		model.addAttribute("results", new ArrayList<>());
		return "searchResults";
	}
}