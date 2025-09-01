package com.pcwk.ehr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.pcwk.ehr.domain.TopicWordsDTO;
import com.pcwk.ehr.service.TopicWordsService;

@Controller
@RequestMapping("/freq/topic")
public class TopicWordsController {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	private MessageSource messageSource;
	
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
	
	private Map<String, String> getWordsMessages(Locale locale) {
		Map<String, String> msgs = new HashMap<>();
		
		msgs.put("keyword", messageSource.getMessage("message.word.keyword", null, locale));
		msgs.put("des1", messageSource.getMessage("message.word.descript1", null, locale));
		msgs.put("des2", messageSource.getMessage("message.word.descript2", null, locale));
		msgs.put("count", messageSource.getMessage("message.word.count", null, locale));
		msgs.put("rate", messageSource.getMessage("message.word.rate", null, locale));
		
		return msgs;
	}
	@GetMapping(value="/words.do")
	public String topicWords(TopicWordsDTO param, Model model,
			@RequestParam(name="lang", required=false) String lang) {
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
		
		 //lang이 값이 없으면 기본값(한국어)
	    String resolvedLang = (lang != null && !lang.isEmpty()) ? lang : "ko";
	    Locale locale = new Locale(resolvedLang);
	    
	    //언어 설정
	    model.addAttribute("msgs", getWordsMessages(locale));
	    model.addAttribute("lang", lang);
		
		return "/freq/topic/words";
		
	}
	
}
