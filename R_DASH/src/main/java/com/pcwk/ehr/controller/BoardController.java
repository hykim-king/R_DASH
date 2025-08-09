package com.pcwk.ehr.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.pcwk.ehr.cmn.MessageDTO;
import com.pcwk.ehr.cmn.PcwkString;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.service.BoardService;
import com.pcwk.ehr.service.MarkdownService;

@Controller
@RequestMapping("/board")
public class BoardController {
	Logger log = LogManager.getLogger(getClass());
	
	@Autowired
	BoardService service;
	
	@Autowired
    private MarkdownService markdownService;

	private final String uploadDir = "/ehr/resources/upload";
	
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
    /**
     * 마크다운을 HTML로 변환하는 API
     * @param markdownText 마크다운 원본
     * @return HTML 문자열
     */
    @PostMapping("/convert")
    public String convertMarkdown(@RequestBody String markdownText) {
        return markdownService.convertToMarkdownHtml(markdownText);
    }
	
	@PostMapping("/imageUpload.do")
	@ResponseBody
	public String imageUpload(MultipartFile file) throws IOException {
	    // 1. 파일 저장 로직
		// 폴더 없으면 생성
	    Path uploadPath = Paths.get(uploadDir);
	    if (Files.notExists(uploadPath)) {
	        Files.createDirectories(uploadPath);
	    }
		
	    String savedFileName = UUID.randomUUID().toString() + PcwkString.getExt(file.getOriginalFilename());
	    Path savePath = Paths.get(uploadDir, savedFileName);
	    file.transferTo(savePath.toFile());

	    // 2. 저장된 이미지 URL 반환
	    String imageUrl = "/resources/upload/" + savedFileName; 
	    return imageUrl;
	}
	
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
		
		BoardDTO outVO = service.doSelectOne(param);
		model.addAttribute("vo", outVO);
		log.debug("outVO: {}"+outVO);
		
		return viewStirng;
	}
	
	
	@GetMapping(value="/doSaveView.do", produces = "text/plain;charset=UTF-8")
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
	
	@GetMapping(value="/doRetrieve.do", produces = "text/plain;charset=UTF-8")
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
	
	@GetMapping(value="/doSelectOne.do", produces = "text/plain;charset=UTF-8")
	public String doSelectOne(BoardDTO param, Model model, HttpServletRequest req) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doSelectOne()*           │");
		log.debug("└───────────────────────────┘");
		log.debug("1. param:{}", param);
		String viewName = "board/board_view";
		
		BoardDTO outVO = service.doSelectOne(param);
		String html = markdownService.convertToMarkdownHtml(outVO.getContents());
		log.debug("2. outVO:{}", outVO);
		
		model.addAttribute("vo", outVO);
		model.addAttribute("contentsTextAreaHtml",html);
		log.debug("3 html: {}",html);
		
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
