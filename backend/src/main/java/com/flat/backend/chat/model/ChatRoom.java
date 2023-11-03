package com.flat.backend.chat.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    private Long roomId;
//    private String roomName;

    private String sender;
    private String receiver;
    private List<ChatMessage> messages;


    public static ChatRoom create(Long roomId, String sender, String receiver) {
        ChatRoom room = new ChatRoom();
        room.setRoomId(roomId);
        room.setSender(sender);
        room.setReceiver(receiver);
        room.setMessages(new ArrayList<>());
        return room;
    }
}
