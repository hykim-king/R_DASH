package com.pcwk.ehr.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
// (선택) 봇 서비스가 있다면 주입
import com.pcwk.ehr.service.BotService; // 없으면 이 import와 필드/코드 제거하세요.
import com.pcwk.ehr.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	private final Logger log = LogManager.getLogger(getClass());

	private final ChatService chatService;
	private final BotService botService; // 없으면 제거

	public ChatController(ChatService chatService, BotService botService) { // BotService 없으면 생성자도 수정
		this.chatService = chatService;
		this.botService = botService;
	}

	/**
	 * 질문 저장 및 답변 반환 (옵션 A: PK 미수령, 에코 응답)
	 */
	@PostMapping("/send")
	public ResponseEntity<ChatDTO> sendChat(@RequestBody @Valid ChatDTO chat,
			@RequestHeader(value = "X-Session-Id", required = false) String sessionId,
			@AuthenticationPrincipal Object principal // CustomUser 사용 시 타입 교체
	) {
		log.debug("┌──────── sendChat ────────┐");
		log.debug("chat={}", chat);
		log.debug("└──────────────────────────┘");

		// 1) 세션/사용자 보정
		chat.setSessionId(sessionId);
		// principal 타입이 CustomUser라면 캐스팅 후 userNo 주입
		// if (principal instanceof CustomUser) {
		// chat.setUserNo(((CustomUser) principal).getUserNo());
		// }

		// 2) 질문 검증
		if (chat.getQuestion() == null || chat.getQuestion().isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		// 3) 봇 호출로 answer 생성 (BotService 없으면 임시 응답 사용)
		String answer;
		try {
			answer = botService != null ? botService.reply(chat.getQuestion(), chat.getSessionId(), chat.getUserNo())
					: "답변 준비 중입니다.";
		} catch (Exception e) {
			log.error("BotService.reply error", e);
			return ResponseEntity.status(502).build(); // Bad Gateway: 외부/봇 오류
		}
		chat.setAnswer(answer);

		// 4) 저장 (PK는 받지 않음)
		int inserted = chatService.insertChat(chat);
		if (inserted != 1) {
			return ResponseEntity.status(500).build();
		}

		// 5) 에코 응답 (logNo/regDt는 포함되지 않을 수 있음)
		return ResponseEntity.ok(chat);
	}

	/**
	 * 특정 logNo의 채팅 조회
	 */
	@GetMapping("/{logNo}")
	public ResponseEntity<ChatDTO> getChat(@PathVariable Long logNo) {
		log.debug("┌──────── getChat ────────┐");
		log.debug("logNo={}", logNo);
		log.debug("└─────────────────────────┘");

		ChatDTO result = chatService.selectChat(logNo);
		return (result != null) ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
	}

	/**
	 * 채팅 목록 조회 (검색 포함)
	 */
	@PostMapping("/list")
	public ResponseEntity<List<ChatDTO>> chatList(@RequestBody SearchDTO search) {
		log.debug("┌──────── chatList ───────┐");
		log.debug("search={}", search);
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
		return (flag == 1) ? ResponseEntity.ok("삭제 성공") : ResponseEntity.status(404).body("삭제 실패");
	}
}