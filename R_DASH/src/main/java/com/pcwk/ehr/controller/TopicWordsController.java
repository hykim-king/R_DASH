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
@RequestMapping("/freq")
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
	
	@GetMapping(value="/topicWords.do", produces = "text/plain;charset=UTF-8")
	public String topicWords(TopicWordsDTO param, Model model) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *topicWords()*            │");
		log.debug("└───────────────────────────┘");
		
		String viewName = "freq/topic_words";
		
		List<TopicWordsDTO> top10 = wordsService.top10(param);
		log.debug("top10: {}"+top10);
		model.addAttribute("top10", top10);
		
		List<TopicWordsDTO> top150 = wordsService.top150(param);
		log.debug("top150: {}"+top150);
		model.addAttribute("top150", top150);
		
		List<TopicWordsDTO> rate = wordsService.getChangeRate(param);
		log.debug("rate: {}"+rate);
		model.addAttribute("rate", rate);
		
		return viewName;
		
	}
	

}
