package com.pcwk.ehr.controller;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.pcwk.ehr.cmn.PcwkString;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.NewsDTO;
import com.pcwk.ehr.domain.TopicDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.service.NewsService;

@Controller
@RequestMapping("/news")
public class NewsController {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	NewsService service;
	
	public NewsController() {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *newsController()*        │");
		log.debug("└───────────────────────────┘");
	}
	
	//1. 토픽 등록 Post0 -> doSaveView(Get)
	//2. 토픽 수정 Post0 -> doUpdateView(Get)
	//3. 토픽 삭제 Post0
	//4. 토픽 조회 Get0
	//5. 토픽 단건 조회 Get0
	//6. 뉴스 키워드 조회 Get0
	//7. 뉴스 전체 조회 Get0
	
	
	@GetMapping("/doUpdateView.do")
	public String doUpdateView(@RequestParam("topicNo") int topicNo,Model model,HttpSession session) throws SQLException{
		log.debug("┌────────────────────────┐");
		log.debug("│ *doUpdateView()*       │");
		log.debug("└────────────────────────┘");
		
		String viewStirng = "news/topic_mod";
		
		TopicDTO vo = new TopicDTO();
	    vo.setTopicNo(topicNo);

	    TopicDTO outVO = service.doSelectOne(vo);
	    model.addAttribute("vo", outVO);
		
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
	//AJAX 호출용
	@GetMapping(value="/topicDetail.do",produces="application/json;charset=UTF-8")
	@ResponseBody
	public TopicDTO getTopicDetail(@RequestParam("topicNo") int topicNo) {
	    TopicDTO topic = new TopicDTO();
	    topic.setTopicNo(topicNo);
	    TopicDTO topicVO = service.doSelectOne(topic);
	    return topicVO;
	}
	
	@GetMapping(value="/newsKeywordSearch.do", produces="application/json;charset=UTF-8")
	@ResponseBody
	public List<NewsDTO> newsKeywordSearch(@RequestParam("keyword") String keyword) {
	    log.debug("┌─────────────────────────────────────────────┐");
	    log.debug("│ *newsKeywordSearch()* keyword=" + keyword +"│");
	    log.debug("└─────────────────────────────────────────────┘");

	    NewsDTO news = new NewsDTO();
	    news.setKeyword(keyword);
	    return service.searchByKeyword(news);
	}
    // JSON 반환용 -> AJAX fetch 호출
//    @GetMapping(value = "/doRetrieve.do", produces = "application/json;charset=UTF-8")
//    public List<NewsDTO> newsRetrieveJson(@RequestParam(defaultValue = "1") int pageNo,
//                                          @RequestParam(defaultValue = "10") int pageSize) {
//        SearchDTO search = new SearchDTO();
//        search.setPageNo(pageNo);
//        search.setPageSize(pageSize);
//        return service.doRetrieve(search);
//    }
    
//	@GetMapping("/news/doRetrieve.do")
//	@ResponseBody
//	public List<NewsDTO> doRetrieve(@RequestParam int pageNo,
//	                                @RequestParam int pageSize) {
//	    SearchDTO searchDTO = new SearchDTO();
//	    searchDTO.setPageNo(pageNo);
//	    searchDTO.setPageSize(pageSize);
//	    return service.doRetrieve(searchDTO);
//	}
	
	@GetMapping(value="/newsPage.do", produces = "text/plain;charset=UTF-8")
	public String newsPage(SearchDTO search,NewsDTO news,TopicDTO topic,Model model) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *newsPage()*              │");
		log.debug("└───────────────────────────┘");
		
		String viewName = "news/news_page";
		// 1. 전체 뉴스
		int pageNo = search.getPageNo() <= 0 ? 1 : search.getPageNo();
	    int pageSize = search.getPageSize() <= 0 ? 10 : search.getPageSize();
	    search.setPageNo(pageNo);
	    search.setPageSize(pageSize);
	    List<NewsDTO> newsList = service.doRetrieve(search);
	    model.addAttribute("newsList", newsList);
	    model.addAttribute("search", search);
	    
	    // * 최종 업데이트 일자 반영 *
	    if(newsList != null && !newsList.isEmpty()) {
	        NewsDTO latest = newsList.get(0); // 최신 뉴스
	        model.addAttribute("latestRegDt", latest.getRegDt());
	    }

	    // 2. 키워드 뉴스 (keyword 파라미터가 있을 때만)
	    if (news.getKeyword() != null && !news.getKeyword().isEmpty()) {
	        List<NewsDTO> keywordNews = service.searchByKeyword(news);
	        model.addAttribute("keywordNews", keywordNews);
	    }
	    // 3. 오늘의 토픽
	    List<TopicDTO> todayTopics = service.getTodayTopicsList(topic);
	    model.addAttribute("todayTopics", todayTopics);

	    // 4. 토픽 단건 조회 (topicNo가 있을 때만) -> 페이지 초기 로딩용
	    if (todayTopics != null && !todayTopics.isEmpty()) {
	        List<TopicDTO> topicDetails = new ArrayList<>();
	        
	        for (TopicDTO t : todayTopics) {
	            int topicNo = t.getTopicNo();
	            TopicDTO topicDetail = new TopicDTO();
	            topicDetail.setTopicNo(topicNo);
	            topicDetail = service.doSelectOne(topicDetail);
	            
	            if (topicDetail != null) {
	                topicDetails.add(topicDetail);
	            }
	        }
	        model.addAttribute("topicDetails",topicDetails);
	    }
	    
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
	public String doSave(TopicDTO param,HttpSession session) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doSave()*                │");
		log.debug("└───────────────────────────┘");
		String jsonString = "";
		
		log.debug("param:{}", param);
				
		UserDTO user = (UserDTO) session.getAttribute("user");
		
//		if(user != null && user.getRole()==1) {
//			param.setRegId(user.getEmail());
//			param.setModId(user.getEmail());
//		}
		
		
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
