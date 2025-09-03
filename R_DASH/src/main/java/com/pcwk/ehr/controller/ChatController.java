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

	public ChatController(ChatService chatService, @Qualifier("GptBotService") BotService botService) {
		this.chatService = chatService;
		this.botService = botService;
	}

	/** 질문 전송 → BotService가 답변 생성 + DB 저장, 컨트롤러는 결과만 반환 */
	@PostMapping(value = "/send", consumes = "application/json", produces = "application/json;charset=UTF-8")
	public ResponseEntity<ChatDTO> sendChat(@RequestBody @Valid ChatDTO chat,
			@RequestHeader(value = "X-Session-Id", required = false) String sessionId,
			@RequestHeader(value = "X-User-No", required = false) Integer userNoHeader,
			@AuthenticationPrincipal Object principal) {

		log.debug("┌──────── sendChat ────────┐");
		log.debug("chat={}", chat);
		log.debug("sessionId(header)={}", sessionId);
		log.debug("userNo(header)={}", userNoHeader);
		log.debug("└──────────────────────────┘");

		// 1) 사용자 번호: principal 최우선 → body/header 보강
		Integer effectiveUserNo = extractUserNo(principal);
		if (effectiveUserNo == null) {
			effectiveUserNo = (chat.getUserNo() != null) ? chat.getUserNo() : userNoHeader;
		}
		log.debug("effectiveUserNo={}", effectiveUserNo);

		// 2) 세션ID 기본값
		final String originalSessionId = sessionId;
		if (sessionId == null || sessionId.trim().isEmpty()) {
			sessionId = UUID.randomUUID().toString();
		}

		// 3) 세션 성격/소유 확인 후 필요 시 분리
		// 3) 세션 성격/소유 확인 후 필요 시 분리
		boolean mustSplit = false;
		try {
			boolean hasGuestLogs = chatService.isGuestSession(sessionId);
			boolean hasUserLogs = chatService.hasAnyUserLogs(sessionId);
			boolean belongsToThisUser = (effectiveUserNo != null)
					&& chatService.existsSessionForUser(sessionId, effectiveUserNo);

			boolean hasAnyLogs = hasGuestLogs || hasUserLogs;
			log.debug("check session. guestLogs={}, belongsToThisUser={}, userLogs={}, anyLogs={}", hasGuestLogs,
					belongsToThisUser, hasUserLogs, hasAnyLogs);

			if (effectiveUserNo != null) {
				// 기존 로그가 있을 때만 분리 판단
				if (hasAnyLogs && (hasGuestLogs || !belongsToThisUser)) {
					mustSplit = true;
				}
			} else {
				if (hasUserLogs)
					mustSplit = true;
			}
		} catch (Exception e) {
			log.warn("session ownership check failed: {}", e.getMessage());
		}

		if (mustSplit) {
			String newSid;
			try {
				newSid = chatService.newSessionId();
			} catch (Exception e) {
				newSid = UUID.randomUUID().toString();
			}
			log.debug("force split session: {} -> {}", originalSessionId, newSid);
			sessionId = newSid;
		}

		// 4) 사용자/세션 세팅
		chat.setUserNo(effectiveUserNo); // 로그인: 숫자, 비로그인: null
		chat.setSessionId(sessionId);

		// 5) 질문 검증
		if (chat.getQuestion() == null || chat.getQuestion().trim().isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		// 6) 봇 호출(저장 포함)
		final String answer;
		try {
			// 로그인 사용자만 DB 저장
			final boolean persist = (effectiveUserNo != null);
			answer = botService.reply(chat.getQuestion(), chat.getSessionId(), chat.getUserNo(), persist);
		} catch (Exception e) {
			log.error("BotService.reply error", e);
			return ResponseEntity.status(502).build();
		}
		chat.setAnswer(answer);

		// 7) 최종 세션ID(+ 유저) 헤더 반환
		ResponseEntity.BodyBuilder resp = ResponseEntity.ok().header("X-Session-Id", sessionId);
		if (effectiveUserNo != null) {
			resp.header("X-User-No", String.valueOf(effectiveUserNo));
		}
		return resp.body(chat);
	}

	/** principal에서 userNo를 꺼내는 유틸 (환경에 맞게 필요시 수정) */
	private Integer extractUserNo(Object principal) {
		if (principal == null)
			return null;
		try {
			// 예: CustomUser#getUserNo()
			java.lang.reflect.Method m = principal.getClass().getMethod("getUserNo");
			Object v = m.invoke(principal);
			if (v instanceof Number)
				return ((Number) v).intValue();
		} catch (Exception ignore) {
		}
		return null;
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
	public ResponseEntity<List<ChatSessionSummary>> sessions(
			@RequestHeader(value = "X-User-No", required = false) Integer userNoHeader,
			@AuthenticationPrincipal Object principal, @RequestParam(defaultValue = "100") int limit) {

		Integer userNo = extractUserNo(principal);
		if (userNo == null)
			userNo = userNoHeader;
		if (userNo == null)
			return ResponseEntity.status(401).build();

		return ResponseEntity.ok(chatService.listSessions(userNo, Math.max(1, Math.min(limit, 200))));
	}

	/** 새 세션 시작(프론트가 받은 세션ID를 이후 /send 호출시 X-Session-Id로 사용) */
	@PostMapping(value = "/sessions/new", produces = "text/plain;charset=UTF-8")
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

	@DeleteMapping("/sessions/{sid}")
	public ResponseEntity<Void> deleteSession(@PathVariable String sid,
			@RequestHeader(value = "X-User-No", required = false) Integer userNoHeader,
			@AuthenticationPrincipal Object principal) {

		// 1) 인증된 사용자 번호 우선
		Integer userNo = extractUserNo(principal);
		if (userNo == null)
			userNo = userNoHeader;
		if (userNo == null)
			return ResponseEntity.status(401).build(); // 로그인 필요

		// 2) 소유 확인 + 삭제
		boolean ok = chatService.deleteSessionForUser(sid, userNo);
		if (!ok)
			return ResponseEntity.status(403).build(); // 내 소유 아님 or 없음

		return ResponseEntity.noContent().build(); // 204
	}
}