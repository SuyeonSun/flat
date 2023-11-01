package com.flat.backend.websocket.controller;

import com.flat.backend.websocket.dto.ChatRoomResponse;
import com.flat.backend.websocket.dto.ChatRoomSaveRequest;
import com.flat.backend.websocket.service.ChatRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/chatRooms")
//@Controller
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    @ResponseBody
    public List<ChatRoomResponse> findAll() {
        return chatRoomService.findAll();
    }

    @PostMapping
    @ResponseBody
    public void addChatRoom(@RequestBody @Valid ChatRoomSaveRequest chatRoomSaveRequest) {
        System.out.println("addChatRoom()...");
        chatRoomService.save(chatRoomSaveRequest.getName());
    }
}
