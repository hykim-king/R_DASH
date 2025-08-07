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
import com.pcwk.ehr.cmn.PcwkString;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	BoardService service;

	public BoardController() {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *BoardController()*       │");
		log.debug("└───────────────────────────┘");
	}
	
//	등록	/board/doSave.do	POST 0
//	수정	/board/doUpdate.do	POST 0
//	삭제	/board/doDelete.do	POST 0
//	상세 조회	/board/doSelectOne.do	GET 0
//	목록 조회	/board/doRetrieve.do	GET 0
//	등록 화면	/board/doSaveView.do	POST
//	수정 화면	/board/doUpdateView.do	POST	
	@GetMapping("/doUpdateView.do")
	public String doUpdateView(@RequestParam("boardNo") int boardNo,Model model,HttpSession session) throws SQLException{
		log.debug("┌────────────────────────┐");
		log.debug("│ *doUpdateView()*       │");
		log.debug("└────────────────────────┘");
		
		String viewStirng = "board/board_mod";
		log.debug("viewStirng: ",viewStirng);
		
		//UserDTO loginUser = (UserDTO) session.getAttribute("user");
		//model.addAttribute("user", loginUser);
		
		BoardDTO param = new BoardDTO();
		param.setBoardNo(boardNo);
		
		BoardDTO dto = service.doSelectOne(param);
		model.addAttribute("dto", dto);
		
		return viewStirng;
	}
	
	
	@GetMapping("/doSaveView.do")
	public String doSaveView(Model model,HttpSession session) {
		log.debug("┌──────────────────────────────┐");
		log.debug("│ *doSaveView()*               │");
		log.debug("└──────────────────────────────┘");
		
		String viewStirng = "board/board_reg";
		log.debug("viewStirng: ",viewStirng);
		
		
		
		//UserDTO loginUser = (UserDTO) session.getAttribute("user");
		//model.addAttribute("user", loginUser);
		
		return viewStirng;
	}
	
	@GetMapping("/doRetrieve.do")
	public String doRetrieve(SearchDTO param, Model model) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doRetrieve()*            │");
		log.debug("└───────────────────────────┘");
		log.debug("1. param:{}", param);
		
		String viewName = "board/board_list";
		
		//페이징
		int pageNo = PcwkString.nvlZero(param.getPageNo(), 1); //0이면 1 반환
		int pageSize = PcwkString.nvlZero(param.getPageSize(), 10); //0이면 10 반환
		
		// 검색 구분
		String searchDiv = PcwkString.nullToEmpty(param.getSearchDiv()); //0이면 null

		// 검색어
		String searchWord = PcwkString.nullToEmpty(param.getSearchWord()); //0이면 null
		
		log.debug("pageNo:{}",pageNo);
		log.debug("pageSize:{}",pageSize);
		log.debug("searchDiv:{}",searchDiv);
		log.debug("searchWord:{}",searchWord);
		
		param.setPageNo(pageNo);
		param.setPageSize(pageSize);
		param.setSearchDiv(searchDiv);
		param.setSearchWord(searchWord);
		
		List<BoardDTO> list = service.doRetrieve(param);
		
		model.addAttribute("list",list);
		model.addAttribute("search", param);
		
		return viewName;
	}
	
	@GetMapping(value="/doSelectOne.do")
	public String doSelectOne(BoardDTO param, Model model, HttpServletRequest req) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doSelectOne()*           │");
		log.debug("└───────────────────────────┘");
		log.debug("1. param:{}", param);
		String viewName = "board/board_view";
		
		BoardDTO outVO = service.doSelectOne(param);
		log.debug("2. outVO:{}", outVO);
		
		model.addAttribute("vo", outVO);
		
		return viewName;
	}
	
	@PostMapping(value = "/doDelete.do", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String doDelete(BoardDTO param, HttpServletRequest req) {
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
	
	@PostMapping(value = "/doUpdate.do", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String doUpdate(BoardDTO param, HttpServletRequest req) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doUpdate()*              │");
		log.debug("└───────────────────────────┘");
		
		log.debug("1. param:{}", param);
		String jsonString = "";
		
		int flag = service.doUpdate(param);
		
		String message = "";
		if (1 == flag) {
			message = "수정 되었습니다.";
		} else {
			message = "수정 실패!";
		}
		MessageDTO messageDTO = new MessageDTO(flag, message);
		jsonString = new Gson().toJson(messageDTO);
		log.debug("jsonString:{}", jsonString); 
		return jsonString;
	}
	
	@PostMapping(value = "doSave.do", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String doSave(BoardDTO param) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doSave()*                │");
		log.debug("└───────────────────────────┘");
		String jsonString = "";
		
		log.debug("param:{}", param);
		
		//1. 이미지를 받으면
		String image = param.getImage(); 
		
		if (image != null && !image.isEmpty()) {
			boolean isImage = PcwkString.isImageExtension(image);
			String imageExt = PcwkString.getExt(image);
			
			//2. 이미지 확장자인지 확인
			if(isImage==false) {
				log.debug("지원하지 않는 확장자 입니다.");
			}	
			//3. uuid + 확장자로 변경
			String savedImageName = PcwkString.getUUID()+imageExt;
			param.setImage(savedImageName);
		}else {
			log.debug("이미지 없음.");
		}
		// 저장 진행
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
