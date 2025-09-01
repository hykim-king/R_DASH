package com.pcwk.ehr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateTest {
	@GetMapping("test")
	public String test() {
		return "login/test";
	}
}
