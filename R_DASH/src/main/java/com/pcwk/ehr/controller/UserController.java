package com.pcwk.ehr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.UserDTO;
import com.pcwk.ehr.mapper.UserMapper;
import com.pcwk.ehr.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	Logger log = LogManager.getLogger(getClass());
	@Autowired
	UserService service;
	@Autowired
	UserMapper mapper;
	
	@GetMapping("userList")
	public String userList(HttpServletRequest request,Model model) {
		String viewName = "user/userList";
		SearchDTO search = new SearchDTO();
		//pageNo 설정
		if(request.getParameter("pageNo")==null||request.getParameter("pageNo").isEmpty()) {
			search.setPageNo(1);
		}else {
			search.setPageNo(Integer.parseInt(request.getParameter("pageNo")));
		}
		//pageSize 20으로 고정
		if(request.getParameter("pageSize")==null) {
			search.setPageSize(10);
		}
		//searchDiv 설정 (10:이메일로 검색, 20:이름으로 검색, 30:관리자만 검색)
		if(request.getParameter("searchDiv")!=null) {
			log.debug("searchDiv:{}",request.getParameter("searchDiv"));
			search.setSearchDiv(request.getParameter("searchDiv"));
		}
		//searchWord 검색어 설정
		if(request.getParameter("searchWord")!=null) {
			search.setSearchWord(request.getParameter("searchWord"));
		}
		
		List<UserDTO> list = service.userList(search);
		
		//총 글수
		int totalCnt = 0;
		
		if(list != null && list.size()>0) {
			UserDTO totalVO = list.get(0);
			totalCnt = totalVO.getTotalCnt();
		}
		
		model.addAttribute("search", search);
		model.addAttribute("list",list);
		model.addAttribute("totalCnt", totalCnt);
		
		return viewName;
	}

	@GetMapping("login")
	public String loginView() {
		String viewName = "user/login";

		return viewName;
	}
	@GetMapping("myPage")
	public String modMyPage(HttpSession session) {
		String viewName = "user/modMyPage";
		if(session.getAttribute("loginUser") == null) {
			return "redirect:/user/login";
		}

		return viewName;
	}
	@GetMapping("changePw")
	public String changePwView(HttpSession session) {
		String viewName = "user/changePw";

		return viewName;
	}
	
	
	@PostMapping(value="/changeRole", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> changeRole(UserDTO param, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		int flag = service.updateRole(param);
		
		if(flag == 1) {
			result.put("success", "1");
			result.put("message", "일반 사용자 권한으로 변경되었습니다.");
		}else if(flag == 2) {
			result.put("success", "2");
			result.put("message", "관리자 권한으로 변경되었습니다.");
		}else {
			result.put("success", "0");
			result.put("message", "변경에 실패했습니다.");
		}
		
		return result;
	}
	
	@PostMapping(value="/changePw", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> changePw(UserDTO param, HttpSession session){
		Map<String, Object> result = new HashMap<>();
		int flag = service.updateUserInfo(param);
		
		if(flag == 1) {
			UserDTO updateUser = service.selectUser(param.getUserNo());
			session.setAttribute("loginUser", updateUser);
			
			result.put("success", true);
			result.put("message","비밀번호가 변경되었습니다.");
		}else {
			result.put("success", false);
			result.put("message","비밀번호 변경에 실패했습니다.");
		}
			
		return result;
	}
	@PostMapping(value="/updateImage", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> updateUserImage(UserDTO param, HttpSession session, MultipartFile file) {
		Map<String, Object> result = new HashMap<>();
		
		int flag = service.updateUserImage(param,file);
		
		if(flag == 1) {
			UserDTO updateUser = service.selectUser(param.getUserNo());
			session.setAttribute("loginUser", updateUser);
			
			result.put("success", true);
			result.put("message","프로필 사진이 변경되었습니다.");
			
		}else {
			result.put("success", false);
			result.put("message","프로필 사진이 변경에 실패하였습니다.");
		}
		
		
		
		return result;
	}
	
	@PostMapping(value="/updateInfo", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> updateUserInfo(UserDTO param, HttpSession session) {
		Map<String, Object> result = new HashMap<>();

		int flag = service.updateUserInfo(param);
		
		if (flag == 1) {
			UserDTO updateUser = service.selectUser(param.getUserNo());
			session.setAttribute("loginUser", updateUser);
			
			result.put("success", true);
			result.put("message", updateUser.getName() + "님 회원정보가 수정되었습니다.");
			
		} else {
			result.put("success", false);
			result.put("message", "수정에 실패했습니다.");
		}
		
		return result;
	}

	@PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> login(UserDTO param, HttpSession session) {
		Map<String, Object> result = new HashMap<>();

		int flag = service.login(param);

		if (flag == 10) {
			result.put("success", false);
			result.put("message", "존재하는 않는 계정입니다.");
		} else if (flag == 20) {
			result.put("success", false);
			result.put("message", "비밀번호가 일치하지 않습니다.");
		} else {
			// 유저 정보 조회
			int userNo = mapper.getUserNo(param.getEmail());
			UserDTO user = service.selectUser(userNo);
			// 세션에 저장
			session.setAttribute("loginUser", user);
			result.put("success", true);
			result.put("message", user.getName() + "환영합니다.");
		}
		log.debug("result:{}", result);

		return result;
	}

	@GetMapping("logout")
	public String logout(HttpSession session) {

		if (null != session.getAttribute("loginUser")) {
			// 서션 삭제
			session.invalidate();
		}

		return "redirect:login";
	}

	@GetMapping("regist")
	public String registView() {
		String viewName = "user/regist";

		return viewName;
	}

	@PostMapping(value = "/regist", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> regist(UserDTO param) {
		Map<String, Object> result = new HashMap<>();
		
		int flag = service.insertUser(param);
		
		if (flag == 1) {
			result.put("success", true);
			result.put("message", param.getName() + "님 가입을 축하합니다!");
		} else {
			result.put("success", false);
			result.put("message", "가입에 실패했습니다.");
		}

		return result;
	}

	@PostMapping(value = "/checkEmail", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> checkEmail(String email) {
		Map<String, Object> result = new HashMap<>();
		int flag = service.checkEmail(email);

		if (flag == 0) {
			result.put("success", true);
			result.put("message", "가능한 이메일입니다.");
		} else {
			result.put("success", false);
			result.put("message", "존재하는 이메일입니다.");
		}
		
		return result;
	}
	
	@PostMapping(value = "/deleteUser", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> deleteUser(String password,HttpSession session) {
		Map<String, Object> result = new HashMap<>();
		//로그인한 사용자 정보에 입력한 비밀번호 적용
		UserDTO user = (UserDTO) session.getAttribute("loginUser");
		user.setPassword(password);
		//비밀번호 확인후 삭제
		int flag = service.deleteUser(user);
		if (flag == 1) {
			//세션에서 제거
			if(session.getAttribute("loginUser")!=null) {
				session.invalidate();
			}
			result.put("success", true);
			result.put("message", "삭제되었습니다.");
		} else {
			result.put("success", false);
			result.put("message", "비밀번호가 일치하지 않습니다.");
		}
		
		return result;
	}

}
