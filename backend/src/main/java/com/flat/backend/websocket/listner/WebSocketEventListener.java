package com.flat.backend.websocket.listner;

import com.flat.backend.websocket.dto.ChatMessage;
import com.flat.backend.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatRoomService chatRoomService;

    @EventListener
    public void handleWebSocketDisconnectListner(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String chatRoomId = (String) headerAccessor.getSessionAttributes().get("chatRoomId");

        if(username != null && chatRoomId != null) {
            chatRoomService.exit(chatRoomId);

            ChatMessage chatMessage =
                    ChatMessage.builder()
                            .type(ChatMessage.MessageType.LEAVE)
                            .sender(username)
                            .build();

            messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
        }
    }
}
