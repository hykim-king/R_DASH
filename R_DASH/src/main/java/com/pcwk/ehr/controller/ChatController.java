package com.pcwk.ehr.controller;

import java.util.List;

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
    private final BotService botService; // 단일 빈 명시 주입

    public ChatController(ChatService chatService,
                          @Qualifier("gptBotService") BotService botService) {
        this.chatService = chatService;
        this.botService  = botService;
    }

    /** 질문 저장 및 답변 반환 */
    @PostMapping("/send")
    public ResponseEntity<ChatDTO> sendChat(
            @RequestBody @Valid ChatDTO chat,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId,
            @AuthenticationPrincipal Object principal // 실제 CustomUser 사용 시 타입 교체
    ) {
        log.debug("┌──────── sendChat ────────┐");
        log.debug("chat={}", chat);
        log.debug("sessionId={}", sessionId);
        log.debug("└──────────────────────────┘");

        // 1) 세션/사용자 보정
        chat.setSessionId(sessionId);
        // if (principal instanceof CustomUser) chat.setUserNo(((CustomUser) principal).getUserNo());

        // 2) 질문 검증(공백 포함)
        if (chat.getQuestion() == null || chat.getQuestion().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 3) 봇 호출로 answer 생성
        final String answer;
        try {
            answer = botService.reply(chat.getQuestion(), chat.getSessionId(), chat.getUserNo());
        } catch (Exception e) {
            log.error("BotService.reply error", e);
            // 외부(봇) 오류는 502로 매핑
            return ResponseEntity.status(502).build();
        }
        chat.setAnswer(answer);

        // 4) 저장 (PK 미수령)
        int inserted = chatService.insertChat(chat);
        if (inserted != 1) {
            return ResponseEntity.status(500).build();
        }

        // 5) 에코 응답
        return ResponseEntity.ok(chat);
    }

    /** 특정 logNo의 채팅 조회 */
    @GetMapping("/{logNo}")
    public ResponseEntity<ChatDTO> getChat(@PathVariable Long logNo) {
        log.debug("getChat logNo={}", logNo);
        ChatDTO result = chatService.selectChat(logNo);
        return (result != null) ? ResponseEntity.ok(result)
                                : ResponseEntity.notFound().build();
    }

    /** 채팅 목록 조회 (검색 포함) */
    @PostMapping("/list")
    public ResponseEntity<List<ChatDTO>> chatList(@RequestBody SearchDTO search) {
        log.debug("chatList search={}", search);
        return ResponseEntity.ok(chatService.chatList(search));
    }

    /** 채팅 삭제 */
    @DeleteMapping("/{logNo}")
    public ResponseEntity<String> deleteChat(@PathVariable Long logNo) {
        int flag = chatService.deleteChat(logNo);
        return (flag == 1) ? ResponseEntity.ok("삭제 성공")
                           : ResponseEntity.status(404).body("삭제 실패");
    }
}