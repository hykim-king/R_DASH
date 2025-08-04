package com.pcwk.ehr.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    final Logger log = LogManager.getLogger(getClass());

    public MainController() {
        log.debug("┌─────────────────────────────────┐");
        log.debug("│ MainController() 생성                            │");
        log.debug("└─────────────────────────────────┘");
    }

    /**
     * 메인 페이지 출력
     * http://localhost:8080/ehr/home
     */
    @GetMapping("/home")
    public String home(Model model) {
        log.debug("┌─────────────────────────────┐");
        log.debug("│ *home()*                    │");
        log.debug("└─────────────────────────────┘");

        model.addAttribute("msg", "재난 알림 웹사이트 '재민이'에 오신 것을 환영합니다!");

        return "main"; // → /WEB-INF/views/main.jsp
    }

}