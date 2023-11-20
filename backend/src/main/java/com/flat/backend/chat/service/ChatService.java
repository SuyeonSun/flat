package com.flat.backend.chat.service;

import com.flat.backend.chat.dto.req.MakeRoomDto;
import com.flat.backend.chat.dto.req.MessageDto;
import com.flat.backend.chat.dto.res.FindRoomsDto;
import com.flat.backend.chat.entity.ChatMessage;
import com.flat.backend.chat.entity.ChatRoom;
import com.flat.backend.chat.repository.ChatMessageRepository;
import com.flat.backend.chat.repository.ChatRoomRepository;
import com.flat.backend.property.repository.PropertyRepository;
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
    private final PropertyRepository propertyRepository;


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
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderAndReceiverAndPropertyId(makeRoomDto.getSender(), makeRoomDto.getReceiver(), makeRoomDto.getPropertyId());
        if(chatRoom.isEmpty()) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok()
                .body(chatRoom.get());
    }

    public ResponseEntity<?> findRooms(String user) {
        Optional<List<ChatRoom>> chatRooms = chatRoomRepository.findByUser(user);
        List<FindRoomsDto> result = new ArrayList<>();

        if(chatRooms.isEmpty()) {
            return ResponseEntity.ok().build();
        }
        else {
            for(ChatRoom cr : chatRooms.get()) {
                FindRoomsDto findRoomsDto = FindRoomsDto.builder()
                        .roomId(cr.getRoomId())
                        .sender(cr.getSender())
                        .receiver(cr.getReceiver())
                        .title(cr.getProperty().getTitle())
                        .image(cr.getProperty().getImage())
                        .build();
                result.add(findRoomsDto);
            }
        }

        return ResponseEntity.ok()
                .body(result);

    }



    //채팅방 생성
    public ResponseEntity<?> createRoom(MakeRoomDto makeRoomDto) {
        ChatRoom chatRoom = ChatRoom.builder()
                .sender(makeRoomDto.getSender())
                .receiver(makeRoomDto.getReceiver())
                .property(propertyRepository.findById(makeRoomDto.getPropertyId()).orElseThrow())
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
