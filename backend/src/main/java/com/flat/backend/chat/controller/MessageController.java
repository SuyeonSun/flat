package com.flat.backend.chat.controller;

import com.flat.backend.chat.model.ChatMessage;
import com.flat.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;
    @MessageMapping("/chat/message")
    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        System.out.println("enter()........");
        chatService.saveMessage(message.getRoomId(), message);
        sendingOperations.convertAndSend("/topic/chat/room/"+message.getRoomId(),message);
    }

    @GetMapping("/chat/messages/{roomId}")
    @ResponseBody
    public List<ChatMessage> getPrevMessages(@PathVariable Long roomId) {
        return chatService.findRoomById(roomId).getMessages();
    }
}
