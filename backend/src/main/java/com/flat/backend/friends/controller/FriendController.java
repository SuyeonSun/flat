package com.flat.backend.friends.controller;

import com.flat.backend.friends.dto.FriendDeleteDto;
import com.flat.backend.friends.dto.RequestDto;
import com.flat.backend.friends.dto.RequestUUIDDto;
import com.flat.backend.friends.service.FriendService;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/friend")
@RequiredArgsConstructor
@Slf4j
public class FriendController {

    private final FriendService friendService;
    private final UserRepository userRepository;

    @PostMapping("/test")
    @ResponseBody
    public ResponseEntity<?> createRequests(@RequestBody RequestDto requestDto) {
        return friendService.sendRequest(requestDto);
    }

    @ResponseBody
    @RequestMapping("/send/{senderId}")
    public ResponseEntity<?> getSendRequests(@PathVariable UUID senderId) {
        User user = userRepository.findById(senderId).orElseThrow();
//        log.info("userId {}", user.getId());
//        log.info("userSentFriendRequest {}", user.getSentFriendRequests().get(0).getReceiverId().toString());

        return friendService.getSendRequests(senderId);
    }

    @ResponseBody
    @RequestMapping("/recv/{receiverId}")
    public ResponseEntity<?> getRecvRequests(@PathVariable UUID receiverId) {
        return friendService.getRecvRequests(receiverId);
    }

    @ResponseBody
    @RequestMapping("/connect")
    public ResponseEntity<?> connectFriend(@RequestBody RequestDto requestDto) {
        return friendService.connectFriend(requestDto);
    }

    @ResponseBody
    @RequestMapping("/reject")
    public ResponseEntity<?> rejectRequest(@RequestBody RequestDto requestDto) {
        return friendService.rejectRequest(requestDto);
    }

    @ResponseBody
    @RequestMapping("/remove")
    public ResponseEntity<?> removeRequest(@RequestBody RequestDto requestDto) {
        return friendService.removeRequest(requestDto);
    }

    @ResponseBody
    @RequestMapping("/list")
    public ResponseEntity<?> getFriendList(@RequestBody RequestUUIDDto requestUUIDDto) {
        log.info("userId = {}", requestUUIDDto.getUserId());
        return friendService.getFriends(requestUUIDDto.getUserId());
    }

    @ResponseBody
    @DeleteMapping
    public ResponseEntity<?> deleteFriend(@RequestBody FriendDeleteDto friendDeleteDto) {
        log.info("friendDeleteDto = {}", friendDeleteDto);
        return friendService.removeFriend(friendDeleteDto);
    }



}
