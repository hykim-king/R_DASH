package com.pcwk.ehr.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
	private MessageSource messageSource;
	
	@Autowired
	BoardService service;
	
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;
	
	private static final String IMAGE_UPLOAD_PATH = "C:/images/summernote/";
	
	
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
//	등록 화면	/board/doSaveView.do	GET 0
//	수정 화면	/board/doUpdateView.do	GET 0
	
	//다국어 소스 담기
	private Map<String, String> getBoardMessages(Locale locale) {
				
	    Map<String, String> msgs = new HashMap<>();
	    msgs.put("saveBoardTitle", messageSource.getMessage("message.board.saveBoardTitle", null, locale));
	    msgs.put("saveBoardComment1", messageSource.getMessage("message.board.saveBoardComment1", null, locale));
	    msgs.put("saveBoardComment2", messageSource.getMessage("message.board.saveBoardComment2", null, locale));
	    msgs.put("saveBoardComment3", messageSource.getMessage("message.board.saveBoardComment3", null, locale));
	    msgs.put("noticeBoard", messageSource.getMessage("message.board.noticeBoard", null, locale));
	    msgs.put("gun", messageSource.getMessage("message.board.gun", null, locale));
	    msgs.put("all", messageSource.getMessage("message.board.all", null, locale));
	    msgs.put("search", messageSource.getMessage("message.board.search", null, locale));
	    msgs.put("reg", messageSource.getMessage("message.board.reg", null, locale));
	    msgs.put("title", messageSource.getMessage("message.board.title", null, locale));
	    msgs.put("contents", messageSource.getMessage("message.board.contents", null, locale));
	    msgs.put("no", messageSource.getMessage("message.board.no", null, locale));
	    msgs.put("regDt", messageSource.getMessage("message.board.regDt", null, locale));
	    msgs.put("view", messageSource.getMessage("message.board.view", null, locale));
	    msgs.put("noBoard", messageSource.getMessage("message.board.noBoard", null, locale));
	    msgs.put("toList", messageSource.getMessage("message.board.toList", null, locale));
	    msgs.put("admin", messageSource.getMessage("message.admin", null, locale));
	    msgs.put("modi", messageSource.getMessage("message.news.mod", null, locale));
	    
	    
	    return msgs;
	}
	
    @PostMapping("/saveImageByUrl")
    @ResponseBody
    public MessageDTO saveImageByUrl(@RequestBody Map<String, String> param) {
        String imageUrl = param.get("imageUrl");
        String publicUrl = "";
        
       
        try {
            if (imageUrl.startsWith("data:image")) {
                // data:image/png;base64,iVBORw0... 이런 식으로 들어옴
                String base64Data = imageUrl.substring(imageUrl.indexOf(",") + 1);
                String meta = imageUrl.substring(0, imageUrl.indexOf(",")); // data:image/png;base64
                String ext = meta.substring(meta.indexOf("/") + 1, meta.indexOf(";"));

                if (ext.equals("jpeg")) ext = "jpg";

                String saveName = UUID.randomUUID().toString() + "." + ext;
                File uploadDir = new File("C:/images/summernote/");
                if (!uploadDir.exists()) uploadDir.mkdirs();

                File file = new File(uploadDir, saveName);
                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                try (OutputStream os = new FileOutputStream(file)) {
                    os.write(imageBytes);
                }


                publicUrl = "/ehr/summernote/" + saveName; // ResourceHandler 매핑 기준 URL
            } else {
                publicUrl = imageUrl;
            }

            return new MessageDTO(1, publicUrl); // messageId=1 성공
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageDTO(0, imageUrl); // 실패 시 원본 반환
        }
    }

    @PostMapping("/uploadSummernoteImageFile")
	@ResponseBody
	public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
	    String originalFilename = file.getOriginalFilename();
	    String ext = PcwkString.getExt(originalFilename);
	    if (ext == null) ext = "";
	    if (!ext.startsWith(".")) ext = "." + ext;

	    String saveName = UUID.randomUUID().toString() + ext;

	    File uploadDir = new File("C:/images/summernote/");
	    if (!uploadDir.exists()) uploadDir.mkdirs();

	    File target = new File(uploadDir, saveName);
	    file.transferTo(target);

	    // 여기서 contextPath 기반이 아니라 ResourceHandler 기반 경로 리턴
	    String publicUrl = "/ehr/summernote/" + saveName;

	    Map<String, Object> result = new HashMap<>();
	    result.put("url", publicUrl);
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
	public String doRetrieve(SearchDTO param, Model model,
			@RequestParam(name="lang", required=false) String lang) {
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
		
		//총 글 수
		int totalCnt = 0;
	    if (!list.isEmpty()) {
	        totalCnt = list.get(0).getTotalCnt(); // 첫 번째 row에서 가져오기
	    }
	    //lang이 값이 없으면 기본값(한국어)
	    String resolvedLang = (lang != null && !lang.isEmpty()) ? lang : "ko";
	    Locale locale = new Locale(resolvedLang);
	    
	    //언어 설정
	    model.addAttribute("msgs", getBoardMessages(locale));
	    model.addAttribute("lang", lang);
	    
		model.addAttribute("list",list);
		model.addAttribute("search", param);
		model.addAttribute("totalCnt", totalCnt);
		
		return viewName;
	}
	
	@GetMapping(value="/doSelectOne.do", produces = "text/plain;charset=UTF-8")
	public String doSelectOne(BoardDTO param, Model model, 
			HttpServletRequest req,
			@RequestParam(name="lang", required=false) String lang) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doSelectOne()*           │");
		log.debug("└───────────────────────────┘");
		log.debug("1. param:{}", param);
		String viewName = "board/board_view";
		
		BoardDTO outVO = service.doSelectOne(param);
		log.debug("2. outVO:{}", outVO);
		
		//lang이 값이 없으면 기본값(한국어)
	    String resolvedLang = (lang != null && !lang.isEmpty()) ? lang : "ko";
	    Locale locale = new Locale(resolvedLang);
	    
	    //언어 설정
	    model.addAttribute("msgs", getBoardMessages(locale));
	    model.addAttribute("lang", lang);
		
		
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
	public String doUpdate(BoardDTO param, HttpSession session,Model model) {
		log.debug("┌───────────────────────────┐");
		log.debug("│ *doUpdate()*              │");
		log.debug("└───────────────────────────┘");
		
		
		
		log.debug("1. param:{}", param);
		String jsonString = "";
		
		UserDTO user = (UserDTO) session.getAttribute("loginUser");
		model.addAttribute("user",user);
		if(user != null && user.getRole()==1) {
			param.setModId(user.getEmail());
		}else {
		    throw new RuntimeException("로그인 필요");
		}
		
		int flag = service.doUpdate(param);
		log.debug("isNotice: {}", param.getIsNotice());
		
		// 공지 글이면 websocket 알림 
	    if("Y".equals(param.getIsNotice())) {
	    	
	    	 // 수정된 데이터 다시 조회
	        BoardDTO updatedVO = service.doSelectOne(param);
	        log.debug("2. updatedVO:{}", updatedVO);
	        
	    	Map<String,String> noticeMsg = new HashMap<>();
	    	noticeMsg.put("boardNo", String.valueOf(param.getBoardNo()));
	    	noticeMsg.put("title", param.getTitle());
	    	
	    	String contents = param.getContents();
	    	if(contents != null && contents.length() > 300) {
	    		contents = contents.substring(0,300)+"...";
	    	}
	    	noticeMsg.put("contents", contents);
	    	
	    	simpMessagingTemplate.convertAndSend("/topic/notice",noticeMsg);
	    }
		
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
	public String doSave(BoardDTO param,HttpSession session,Model model) {
	    log.debug("param:{}", param);
	    log.debug("isNotice: {}", param.getIsNotice());
	    int flag = 0;
	    
	    try {
	        UserDTO user = (UserDTO) session.getAttribute("loginUser");
	        log.debug("user: {}",user);
			model.addAttribute("user",user);
			if(user != null && user.getRole()==1) {
				param.setRegId(user.getEmail());
				param.setModId(user.getEmail());
			}else {
			    throw new RuntimeException("로그인 필요");
			}
	    	
	        flag = service.doSave(param);
	    } catch (Exception e) {
	        log.error("DB 저장 오류", e);
	    }
	    //param = service.doSelectOne(param);
	    
	    if(flag == 1) {
		    // 공지 글이면 websocket 알림 
		    if("Y".equals(param.getIsNotice())) {
		    	Map<String,String> noticeMsg = new HashMap<>();
		    	noticeMsg.put("boardNo", String.valueOf(param.getBoardNo()));
		    	noticeMsg.put("title", param.getTitle());
		    	
		    	String contents = param.getContents();
		    	if(contents != null && contents.length() > 300) {
		    		contents = contents.substring(0,300)+"...";
		    	}
		    	noticeMsg.put("contents", contents);
		    	
		    	simpMessagingTemplate.convertAndSend("/topic/notice",noticeMsg);
		    }
	    }
	    
	    log.debug("boardNo: {}", param.getBoardNo());
	    log.debug("title: {}", param.getTitle());
	    log.debug("isNotice: {}", param.getIsNotice());
	    
	    String message = "";

	    if (1 == flag) {
			message = "등록 되었습니다.";
		} else {
			message = "등록 실패!";
		}
	    MessageDTO messageDTO = new MessageDTO(flag, message);
	    return new Gson().toJson(messageDTO);
	}
	

}