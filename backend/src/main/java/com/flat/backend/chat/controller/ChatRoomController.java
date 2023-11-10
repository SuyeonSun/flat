package com.flat.backend.chat.controller;

import com.flat.backend.chat.dto.req.MakeRoomDto;
import com.flat.backend.chat.entity.ChatRoom;
import com.flat.backend.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<?> createRoom(@RequestBody MakeRoomDto makeRoomDto) {
        return chatService.createRoom(makeRoomDto);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<?> roomInfo(@PathVariable Long roomId) {
        return chatService.getRoom(roomId);
    }

    @PostMapping("/find/room")
    @ResponseBody
    public ResponseEntity<?> findRoom(@RequestBody MakeRoomDto makeRoomDto) {
        return chatService.findRoom(makeRoomDto);
    }

    @PostMapping("/find/rooms")
    @ResponseBody
    public ResponseEntity<?> findRooms(@RequestBody Map<String, String> userMap) {
        return chatService.findRooms(userMap.get("user"));
    }

}
