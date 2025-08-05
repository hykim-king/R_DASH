package com.pcwk.ehr.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private static final Logger log = LogManager.getLogger(MainController.class);

    public MainController() {
        log.debug("┌─────────────────────────────────────┐");
        log.debug("│ MainController() 생성자 호출        │");
        log.debug("└─────────────────────────────────────┘");
    }

    /**
     * 메인 화면 출력
     * URL: http://localhost:8080/ehr/home
     */
    @GetMapping("/home")
    public String home(Model model) {
        log.debug("┌─────────────────────┐");
        log.debug("│ home() 메서드 실행  │");
        log.debug("└─────────────────────┘");

        model.addAttribute("msg", "저희 재난 알림 사이트를 방문해주셔서 감사합니다.");
        return "main"; // /WEB-INF/views/main.jsp
    }

    /**
     * 검색 처리
     * URL: http://localhost:8080/ehr/search?keyword=검색어
     */
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        log.debug("검색 요청 keyword: {}", keyword);

        // 여기서 DB 검색 또는 API 연동 로직 수행
        // 예: 검색어를 그대로 결과로 반환
        model.addAttribute("keyword", keyword);
        model.addAttribute("results", null); // 결과 리스트 넣기

        return "searchResults"; // /WEB-INF/views/searchResults.jsp
    }
}