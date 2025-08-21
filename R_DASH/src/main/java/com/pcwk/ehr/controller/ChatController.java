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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pcwk.ehr.cmn.SearchDTO;
import com.pcwk.ehr.domain.ChatDTO;
import com.pcwk.ehr.domain.ChatSessionSummary;
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
			@RequestHeader(value = "X-User-No", required = false) Integer userNoHeader, // ★ 추가
			@AuthenticationPrincipal Object principal // 실제 CustomUser 사용 시 타입 교체
	) {
		log.debug("┌──────── sendChat ────────┐");
		log.debug("chat={}", chat);
		log.debug("sessionId(header)={}", sessionId);
		log.debug("userNo(header)={}", userNoHeader);
		log.debug("└──────────────────────────┘");

		// 1) 세션/사용자 보정
		if (sessionId == null || sessionId.trim().isEmpty()) {
			sessionId = UUID.randomUUID().toString(); // 컨트롤러에서 생성해 클라이언트에 돌려줌
		}
		chat.setSessionId(sessionId);

		// 로그인 정보를 쓰는 경우:
		// if (principal instanceof CustomUser) chat.setUserNo(((CustomUser)
		// principal).getUserNo());
		// 헤더 값이 들어왔다면 우선 적용 (비로그인/임시 처리용)
		if (chat.getUserNo() == null && userNoHeader != null) {
			chat.setUserNo(userNoHeader);
		}

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

	/** (기존) 세션별 최근 N개 히스토리 */
	@GetMapping(value = "/history", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<ChatDTO>> history(
			@RequestHeader(value = "X-Session-Id", required = false) String sessionId,
			@RequestHeader(value = "X-User-No", required = false) Integer userNo,
			@RequestParam(defaultValue = "50") int limit) {

		if (sessionId == null || sessionId.trim().isEmpty()) {
			return ResponseEntity.badRequest().build();
		}
		List<ChatDTO> list = chatService.findRecentBySession(sessionId, userNo, Math.max(1, Math.min(limit, 200)));
		java.util.Collections.reverse(list); // 오래된→최신
		return ResponseEntity.ok(list);
	}

	// ========= 아래 3개 엔드포인트가 "좌측 방 목록 + 방별 메시지"에 필요 =========

	/** 좌측 패널: 내 세션(방) 목록 (최근 대화 순) */
	@GetMapping(value = "/sessions", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<ChatSessionSummary>> sessions(@RequestHeader("X-User-No") Integer userNo,
			@RequestParam(defaultValue = "100") int limit) {
		return ResponseEntity.ok(chatService.listSessions(userNo, Math.max(1, Math.min(limit, 200))));
	}

	/** 새 세션 시작(프론트가 받은 세션ID를 이후 /send 호출시 X-Session-Id로 사용) */
	@PostMapping(value = "/sessions/new", produces = "application/json;charset=UTF-8")
	public ResponseEntity<String> newSession() {
		return ResponseEntity.ok(chatService.newSessionId());
	}

	/** 특정 세션의 메시지 페이지(무한스크롤: beforeLogNo < 가장 오래 로드된 logNo) */
	@GetMapping(value = "/sessions/{sessionId}/messages", produces = "application/json;charset=UTF-8")
	public ResponseEntity<List<ChatDTO>> messages(@PathVariable String sessionId,
			@RequestHeader(value = "X-User-No", required = false) Integer userNo,
			@RequestParam(required = false) Long beforeLogNo, @RequestParam(defaultValue = "30") int limit) {

		List<ChatDTO> rows = chatService.listMessagesBySession(sessionId, userNo, beforeLogNo,
				Math.max(1, Math.min(limit, 200)));
		java.util.Collections.reverse(rows); // 오래된→최신으로 반환
		return ResponseEntity.ok(rows);
	}
}