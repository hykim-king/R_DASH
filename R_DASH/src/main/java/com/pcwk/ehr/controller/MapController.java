package com.pcwk.ehr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapController {

	@GetMapping("/map")
	public String mapPage() {
		// → /WEB-INF/views/map/map.jsp (tiles면 "map" definition)
		return "map/map";
	}
}