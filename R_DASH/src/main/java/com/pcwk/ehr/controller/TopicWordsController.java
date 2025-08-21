package com.pcwk.ehr.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pcwk.ehr.domain.TopicWordsDTO;
import com.pcwk.ehr.service.TopicWordsService;

@Controller
@RequestMapping("/freq/topic")
public class TopicWordsController {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	TopicWordsService wordsService;

	public TopicWordsController() {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *TopicWordsController()*  │");
		log.debug("└───────────────────────────┘");
	}
	
	//top10	GET
	//top150	GET
	//getChangeRate	GET
	
	@GetMapping(value="/words.do")
	public String topicWords(TopicWordsDTO param, Model model) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *words()*                 │");
		log.debug("└───────────────────────────┘");
				
		List<TopicWordsDTO> top10 = wordsService.top10();
		log.debug("top10: {}"+top10);
		model.addAttribute("top10", top10);
		
		List<TopicWordsDTO> top100 = wordsService.top100();
		log.debug("top100: {}"+top100);
		model.addAttribute("cloud", top100);
		
		List<TopicWordsDTO> rate = wordsService.getChangeRate();
		log.debug("rate: {}"+rate);
		model.addAttribute("rate", rate);
		
		return "/freq/topic/words";
		
	}
	
}
