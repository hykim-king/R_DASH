package com.pcwk.ehr.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
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
import com.google.gson.JsonObject;
import com.pcwk.ehr.cmn.MessageDTO;
import com.pcwk.ehr.cmn.PcwkString;
import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.BoardDTO;
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
	
    @PostMapping(value="/boardImageFile", produces="application/json")
    @ResponseBody
    public Map<String, Object> boardImageFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> result = new HashMap<>();

        String fileRoot = "C:/summernote_image/"; // 반드시 폴더 존재 확인
        File dir = new File(fileRoot);
        if (!dir.exists()) dir.mkdirs(); // 폴더 없으면 생성

        try {
            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String savedFileName = UUID.randomUUID().toString() + extension;

            File targetFile = new File(fileRoot + savedFileName);
            file.transferTo(targetFile); // Spring MultipartFile 기본 제공

            result.put("url", "/summernoteImage/" + savedFileName);
            result.put("responseCode", "success");

        } catch (Exception e) {
            e.printStackTrace();
            result.put("responseCode", "error");
            result.put("message", e.getMessage());
        }

        return result;
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
	
	//UUID 새로 만들지 말고, Summernote 업로드 시 반환된 URL 그대로 사용
	@PostMapping(value = "doSave.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String doSave(BoardDTO param) {
	    log.debug("param:{}", param);

	    String image = param.getImage(); 
	    if (image != null && !image.isEmpty()) {
	        // 이미지 URL 그대로 사용
	        log.debug("이미지 있음: {}", image);
	    } else {
	        log.debug("이미지 없음.");
	    }

	    int flag = service.doSave(param); // DB 저장
	    String message = (flag == 1) ? param.getTitle() + " 등록되었습니다." : param.getTitle() + " 등록 실패";
	    
	    MessageDTO messageDTO = new MessageDTO(flag, message);
	    return new Gson().toJson(messageDTO);
	}
	

}
