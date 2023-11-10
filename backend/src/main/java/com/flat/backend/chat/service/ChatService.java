package com.flat.backend.chat.service;

import com.flat.backend.chat.dto.req.MakeRoomDto;
import com.flat.backend.chat.dto.req.MessageDto;
import com.flat.backend.chat.entity.ChatMessage;
import com.flat.backend.chat.entity.ChatRoom;
import com.flat.backend.chat.repository.ChatMessageRepository;
import com.flat.backend.chat.repository.ChatRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    //채팅방 정보 가져오기
    public ResponseEntity<?> getRoom(Long roomId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        if(chatRoom.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .body(chatRoom.get());
    }

    public ResponseEntity<?> findRoom(MakeRoomDto makeRoomDto) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderAndReceiver(makeRoomDto.getSender(), makeRoomDto.getReceiver());
        if(chatRoom.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok()
                .body(chatRoom.get());
    }

    public ResponseEntity<?> findRooms(String user) {
        Optional<List<ChatRoom>> chatRooms = chatRoomRepository.findByUser(user);

        if(chatRooms.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok()
                .body(chatRooms.get());

    }



    //채팅방 생성
    public ResponseEntity<?> createRoom(MakeRoomDto makeRoomDto) {
        ChatRoom chatRoom = ChatRoom.builder()
                .sender(makeRoomDto.getSender())
                .receiver(makeRoomDto.getReceiver())
                .build();

        chatRoomRepository.save(chatRoom);
        return ResponseEntity.ok()
                .body(chatRoom);
    }

    @Transactional
    public void saveMessage(Long roomId, MessageDto message) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        if(chatRoom.isEmpty()) {
            return;
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .type(message.getType())
                .sender(message.getSender())
                .message(message.getMessage())
                .date(message.getDate())
                .chatRoom(chatRoomRepository.findById(roomId).orElseThrow())
                .build();


        chatMessageRepository.save(chatMessage);

        chatRoom.get().getMessages().add(chatMessage);
        log.info("messages size = {}", chatRoom.get().getMessages().size());
    }

    public ResponseEntity<?> getPrevMessages(Long roomId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findById(roomId);
        if(chatRoom.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok()
                .body(chatRoom.get().getMessages());
    }
}
