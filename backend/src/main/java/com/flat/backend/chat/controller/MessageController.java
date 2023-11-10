package com.flat.backend.chat.controller;

import com.flat.backend.chat.dto.req.MessageDto;
import com.flat.backend.chat.entity.ChatMessage;
import com.flat.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;
    @MessageMapping("/chat/message")
    public void enter(MessageDto message) {
//        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
//            message.setMessage(message.getSender()+"님이 입장하였습니다.");
//        }

        System.out.println("enter()........");
        chatService.saveMessage(message.getRoomId(), message);

        sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(), message);
    }

    @PostMapping("/chat/messages")
    @ResponseBody
    public ResponseEntity<?> getPrevMessages(@RequestBody Map<String, Long> roomIdMap) {
        return chatService.getPrevMessages(roomIdMap.get("roomId"));
    }
}
