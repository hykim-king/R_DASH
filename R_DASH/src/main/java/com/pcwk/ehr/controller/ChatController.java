package com.pcwk.ehr.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.service.ChatService;

@RestController // @Controller + @ResponseBody
@RequestMapping("/api/chat") //
public class ChatController {

	private final Logger log = LogManager.getLogger(getClass());

	@Autowired
	private ChatService chatService;

	/**
	 * 질문 저장 및 답변 반환 (예: 챗봇 사용)
	 */
	@PostMapping("/send") // 실제 경로: /api/chat/send
	public ResponseEntity<ChatDTO> sendChat(@RequestBody ChatDTO chat) {
		log.debug("┌──────── sendChat ────────┐");
		log.debug("chat = {}", chat);
		log.debug("└──────────────────────────┘");

		// 채팅 저장
		int result = chatService.insertChat(chat);
		if (result == 1) {
			// 저장된 질문 다시 조회 (logNo 포함)
			SearchDTO search = new SearchDTO(1, 1, "10", chat.getQuestion());
			List<ChatDTO> chatList = chatService.chatList(search);

			if (!chatList.isEmpty()) {
				return ResponseEntity.ok(chatList.get(0));
			}
		}

		return ResponseEntity.status(500).body(null);
	}

	/**
	 * 특정 logNo의 채팅 조회
	 */
	@GetMapping("/{logNo}") // 실제 경로: /api/chat/5
	public ResponseEntity<ChatDTO> getChat(@PathVariable Long logNo) {
		log.debug("┌──────── getChat ────────┐");
		log.debug("logNo = {}", logNo);
		log.debug("└─────────────────────────┘");

		ChatDTO result = chatService.selectChat(logNo);
		if (result != null) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 채팅 목록 조회 (검색 포함)
	 */
	@PostMapping("/list")
	public ResponseEntity<List<ChatDTO>> chatList(@RequestBody SearchDTO search) {
		log.debug("┌──────── chatList ───────┐");
		log.debug("search = {}", search);
		log.debug("└─────────────────────────┘");

		List<ChatDTO> list = chatService.chatList(search);
		return ResponseEntity.ok(list);
	}

	/**
	 * 채팅 삭제
	 */

	@DeleteMapping("/{logNo}")
	public ResponseEntity<String> deleteChat(@PathVariable Long logNo) {
		int flag = chatService.deleteChat(logNo);
		if (flag == 1) {
			return ResponseEntity.ok("삭제 성공");
		} else {
			return ResponseEntity.status(404).body("삭제 실패");
		}
	}
}