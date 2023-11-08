package com.flat.backend.websocket.dto;

import com.flat.backend.websocket.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ChatRoomResponse {

    private final String id;
    private final String name;
    private final int count;
    private final LocalDateTime expiryDate;

    public ChatRoomResponse(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
        this.count = chatRoom.getCount();
        this.expiryDate = chatRoom.getExpiryDate();
    }
}
