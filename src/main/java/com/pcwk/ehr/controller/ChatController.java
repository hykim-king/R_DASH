package com.pcwk.ehr.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.pcwk.ehr.service.BotService;
import com.pcwk.ehr.service.ChatService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	private final Logger log = LogManager.getLogger(getClass());
	private final ChatService chatService;
	private final BotService botService;

	public ChatController(ChatService chatService, @Qualifier("gptBotService") BotService botService) {
		this.chatService = chatService;
		this.botService = botService;
	}

	/** 질문 전송 → BotService가 답변 생성 + DB 저장, 컨트롤러는 결과만 반환 */
	@PostMapping(value = "/send", consumes = "application/json", produces = "application/json;charset=UTF-8")
	public ResponseEntity<ChatDTO> sendChat(@RequestBody @Valid ChatDTO chat,
			@RequestHeader(value = "X-Session-Id", required = false) String sessionId,
			@AuthenticationPrincipal Object principal // 실제 CustomUser 사용 시 타입 교체
	) {
		log.debug("┌──────── sendChat ────────┐");
		log.debug("chat={}", chat);
		log.debug("sessionId(header)={}", sessionId);
		log.debug("└──────────────────────────┘");

		// 1) 세션/사용자 보정
		if (sessionId == null || sessionId.trim().isEmpty()) {
			sessionId = UUID.randomUUID().toString(); // 컨트롤러에서 생성해 클라이언트에 돌려줌
		}
		chat.setSessionId(sessionId);
		// if (principal instanceof CustomUser) chat.setUserNo(((CustomUser)
		// principal).getUserNo());

		// 2) 질문 검증
		if (chat.getQuestion() == null || chat.getQuestion().trim().isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		// 3) 봇 호출(→ 내부에서 DB 저장까지 수행)
		final String answer;
		try {
			answer = botService.reply(chat.getQuestion(), chat.getSessionId(), chat.getUserNo());
		} catch (Exception e) {
			log.error("BotService.reply error", e);
			return ResponseEntity.status(502).build(); // Bad Gateway
		}
		chat.setAnswer(answer);

		// 4) 여기서 추가 INSERT 금지 (BotService에서 이미 저장함)
		// int inserted = chatService.insertChat(chat);

		// 5) 응답 (세션ID를 헤더로 돌려주면 프론트에서 이어서 사용하기 좋음)
		return ResponseEntity.ok().header("X-Session-Id", sessionId).body(chat);
	}

	/** 특정 logNo의 채팅 조회 */
	@GetMapping(value = "/{logNo}", produces = "application/json;charset=UTF-8")
	public ResponseEntity<ChatDTO> getChat(@PathVariable Long logNo) {
		log.debug("getChat logNo={}", logNo);
		ChatDTO result = chatService.selectChat(logNo);
		return (result != null) ? ResponseEntity.ok(result) : ResponseEntity.notFound().build();
	}

	/** 채팅 목록 조회 (검색 포함) */
	@PostMapping(value = "/list", consumes = "application/json", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<ChatDTO>> chatList(@RequestBody SearchDTO search) {
		log.debug("chatList search={}", search);
		return ResponseEntity.ok(chatService.chatList(search));
	}

	/** 채팅 삭제 */
	@DeleteMapping("/{logNo}")
	public ResponseEntity<String> deleteChat(@PathVariable Long logNo) {
		int flag = chatService.deleteChat(logNo);
		return (flag == 1) ? ResponseEntity.ok("삭제 성공") : ResponseEntity.status(404).body("삭제 실패");
	}
}