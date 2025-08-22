package com.pcwk.ehr.controller;

import org.springframework.stereotype.Controller;
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

}