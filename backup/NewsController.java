package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.MessageDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.domain.NewsDTO;
import com.pcwk.ehr.domain.TopicDTO;
import com.pcwk.ehr.service.NewsService;

@Controller
@RequestMapping("/news")
public class newsController {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	NewsService service;
	
	public newsController() {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *newsController()*        │");
		log.debug("└───────────────────────────┘");
	}
	
	//1. 토픽 등록 Post0 -> doSaveView(Get)
	//2. 토픽 수정 Post0 -> doUpdateView(Get)
	//3. 토픽 삭제 Post0
	//4. 토픽 조회 Get0
	//5. 토픽 단건 조회 Get0
	//6. 뉴스 조회 Get0
	
	@GetMapping("/doUpdateView.do")
	public String doUpdateView(@RequestParam("topicNo") int topicNo,Model model,HttpSession session) throws SQLException{
		log.debug("┌────────────────────────┐");
		log.debug("│ *doUpdateView()*       │");
		log.debug("└────────────────────────┘");
		
		String viewStirng = "news/topic_mod";
		log.debug("viewStirng: ",viewStirng);
		
		//UserDTO loginUser = (UserDTO) session.getAttribute("user");
		//model.addAttribute("user", loginUser);
		
		TopicDTO param = new TopicDTO();
		param.setTopicNo(topicNo);
		
		TopicDTO outVO = service.doSelectOne(param);
		model.addAttribute("vo", outVO);
		log.debug("outVO: {}"+outVO);
		
		return viewStirng;
	}
	
	@GetMapping(value="/doSaveView.do", produces = "text/plain;charset=UTF-8")
	public String doSaveView(Model model,HttpSession session) {
		log.debug("┌──────────────────────────────┐");
		log.debug("│ *doSaveView()*               │");
		log.debug("└──────────────────────────────┘");
		
		String viewStirng = "news/topic_reg";
		log.debug("viewStirng: ",viewStirng);
		
		
		
		//UserDTO loginUser = (UserDTO) session.getAttribute("user");
		//model.addAttribute("user", loginUser);
		
		return viewStirng;
	}
	
	@GetMapping(value="getNews.do",produces = "text/plain;charset=UTF-8")
	public String searchByKeyword(NewsDTO param,Model model) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *getTodayTopicsList()*    │");
		log.debug("└───────────────────────────┘");
		log.debug("1. param:{}", param);
		
		String viewName = "news/news_page";
		
		String keyword = param.getKeyword();
		log.debug("keyword:{}",keyword);
		
		param.setKeyword(keyword);
		
		List<NewsDTO> list = service.searchByKeyword(param);
		
		model.addAttribute("list",list);
		
		return viewName;
	}
	
	@GetMapping(value="/todayTopic.do", produces = "text/plain;charset=UTF-8")
	public String getTodayTopicsList(TopicDTO param,Model model) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *getTodayTopicsList()*    │");
		log.debug("└───────────────────────────┘");
		log.debug("1. param:{}", param);
		
		String viewName = "news/news_page";
		
		List<TopicDTO> list = service.getTodayTopicsList(param);
		
		model.addAttribute("list",list);
		
		return viewName;
	}
	
	@GetMapping(value="/topicSelectOne.do", produces = "text/plain;charset=UTF-8")
	public String topicSelectOne(TopicDTO param,Model model,HttpServletRequest req) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *topicSelectOne()*        │");
		log.debug("└───────────────────────────┘");
		log.debug("1. param:{}", param);
		String viewName = "news/news_page";
		
		TopicDTO outVO = service.doSelectOne(param);
		log.debug("2. outVO:{}", outVO);
		
		model.addAttribute("vo", outVO);
		return viewName;
		
	}
	
	@PostMapping(value = "/doDelete.do", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String doDelete(TopicDTO param, HttpServletRequest req) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doDelete()*              │");
		log.debug("└───────────────────────────┘");
		log.debug("1. param:{}", param);
		String jsonString = "";
		int flag = service.doDelete(param);

		String message = "";
		if (1 == flag) {
			message = "삭제 되었습니다.";
		} else {
			message = "삭제 실패!";
		}

		jsonString = new Gson().toJson(new MessageDTO(flag, message));
		log.debug("2.jsonString:{}", jsonString);
		return jsonString;
	}
	
	@PostMapping(value = "doUpdate.do", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String doUpdate(TopicDTO param) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doSave()*                │");
		log.debug("└───────────────────────────┘");
		String jsonString = "";
		
		log.debug("param:{}", param);
		
		int flag = service.doUpdate(param);
		
		String message = "";
		if (1 == flag) {
			message = param.getTitle() + "등록되었습니다.";
		} else {
			message = param.getTitle() + "등록 실패";
		}
		
		MessageDTO messageDTO = new MessageDTO(flag, message);
		jsonString = new Gson().toJson(messageDTO);
		log.debug("jsonString:{}", jsonString);
		
		return jsonString;
	}
	
	@PostMapping(value = "doSave.do", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String doSave(TopicDTO param) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doSave()*                │");
		log.debug("└───────────────────────────┘");
		String jsonString = "";
		
		log.debug("param:{}", param);
		
		int flag = service.doSave(param);
		
		String message = "";
		if (1 == flag) {
			message = param.getTitle() + "등록되었습니다.";
		} else {
			message = param.getTitle() + "등록 실패";
		}
		
		MessageDTO messageDTO = new MessageDTO(flag, message);
		jsonString = new Gson().toJson(messageDTO);
		log.debug("jsonString:{}", jsonString);
		
		return jsonString;
	}
	 
	

}
