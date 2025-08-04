package com.pcwk.ehr.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private static final Logger log = LogManager.getLogger(MainController.class);

    public MainController() {
        log.debug("┌──────────────────────────────────────────────┐");
        log.debug("│ MainController() 생성자 호출                           │");
        log.debug("└──────────────────────────────────────────────┘");
    }

    /**
     * 메인 화면 출력
     * 요청 URL: http://localhost:8080/ehr/home
     * @param model
     * @return main.jsp
     */
    @GetMapping("/home")
    public String home(Model model) {
        log.debug("┌─────────────────────────────┐");
        log.debug("│ home() 메서드 실행             │");
        log.debug("└─────────────────────────────┘");

        // 메시지를 JSP로 전달
        model.addAttribute("msg", "저희 재난 알림 사이트를 방문해주셔서 감사합니다.");

        return "main/main"; // → /WEB-INF/views/main/main.jsp
    }
}