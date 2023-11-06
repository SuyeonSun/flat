package com.flat.backend.chat.service;

import com.flat.backend.chat.dto.req.MakeRoomDto;
import com.flat.backend.chat.model.ChatMessage;
import com.flat.backend.chat.model.ChatRoom;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Long id = 0L;
    private Map<Long, ChatRoom> chatRooms;

    @PostConstruct
    //의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    //채팅방 불러오기
    public List<ChatRoom> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
        List<ChatRoom> result = new ArrayList<>(chatRooms.values());
        Collections.reverse(result);

        return result;
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(String roomId) {
        return chatRooms.get(roomId);
    }

    public String findRoomBySenderAndReceiver(MakeRoomDto makeRoomDto) {
        String res = "";
        for(ChatRoom room : chatRooms.values()) {
            if(room.getSender().equals(makeRoomDto.getSender()) && room.getReceiver().equals(makeRoomDto.getReceiver())) {
                res = room.getRoomId().toString();
                System.out.println("roomId = " + room.getRoomId());
                break;
            }
        }
        return res;
    }

    //채팅방 생성
    public ChatRoom createRoom(MakeRoomDto makeRoomDto) {
        ChatRoom chatRoom = ChatRoom.create(++this.id, makeRoomDto.getSender(), makeRoomDto.getReceiver());
        chatRooms.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    public void saveMessage(Long roomId, ChatMessage message) {
        ChatRoom room = chatRooms.get(roomId);
        List<ChatMessage> messages = room.getMessages();
        messages.add(message);
    }

    public ChatRoom findRoomById(Long roomId) {
        return chatRooms.get(roomId);
    }
}
