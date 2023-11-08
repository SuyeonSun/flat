package com.flat.backend.websocket.controller;


import com.flat.backend.websocket.dto.ChatMessage;
import com.flat.backend.websocket.service.ChatRoomService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
//@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/{chatRoomId}/chat.sendMessage")
    public void sendMessage(@Payload @Valid ChatMessage chatMessage, @DestinationVariable String chatRoomId) {
        template.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
    }

    @MessageMapping("/{chatRoomId}/chat.addUser")
    public void addUser(@DestinationVariable String chatRoomId, @Payload @NotNull ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {

        // 채팅방 인원 추가
        chatRoomService.enter(chatRoomId);

        // 구독한 채팅방에 입장 메시지 보내기
        headerAccessor.getSessionAttributes().put("chatRoomId", chatRoomId);
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        template.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
    }
}
