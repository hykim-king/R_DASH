package com.pcwk.ehr.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

	@GetMapping("/map")
	public String map() {
		return "map/map";
	}

	// /map/dust -> /map?layer=dust&airType=ALL (컨텍스트는 Spring이 자동으로 붙임)
	@GetMapping("/map/dust")
	public String dustRedirect() {
		return "redirect:/map?layer=dust&airType=ALL";
	}

	@GetMapping("/map/shelter")
	public String shelterRedirect() {
		return "redirect:/map?layer=shelter";

	}

	@GetMapping("/map/firestation")
	public String firestationRedirect() {
		return "redirect:/map?layer=firestation";
	}

	 @GetMapping
	    public String map(Model model, HttpServletRequest req) {
	        // 쿼리파라미터 layer를 JSP로 전달 (기본: none)
	        String layer = req.getParameter("layer");
	        model.addAttribute("layer", (layer == null ? "none" : layer));
	        return "map/map";  // -> /WEB-INF/views/map/map.jsp
	    }
	
	 @GetMapping("/map/landslide")
	    public String landslideRedirect() {
	        // 필요 시 클러스터 기본 ON: &cluster=on (옵션)
	        return "redirect:/map?layer=landslide";
	    }

	    @GetMapping("/map/sinkhole")
	    public String sinkholeRedirect() {
	        // 필요 시 클러스터 기본 ON: &cluster=on (옵션)
	        return "redirect:/map?layer=sinkhole";
	    }

}