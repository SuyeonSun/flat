package com.flat.backend.user.controller;

import com.flat.backend.common.BaseException;
import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.friends.dto.SendReqDto;
import com.flat.backend.friends.repository.entity.ReqFriendDto;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.flat.backend.common.BaseResponseStatus.INVALID_USER_INFO;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> getUser(@RequestBody Map<String, String> emailMap) {
        Optional<User> user = userRepository.findByEmail(emailMap.get("email"));

        if(user.isEmpty()) {
            return ResponseEntity.ok()
                    .body(new BaseException(INVALID_USER_INFO));
        }

        return ResponseEntity.ok()
                .body(user.get());
    }

    @ResponseBody
    @PostMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestBody Map<String, String> nameMap) {
        Optional<User> user = userRepository.findByName(nameMap.get("name"));

        if(user.isEmpty()) {
            return ResponseEntity.ok()
                    .body(new BaseException(INVALID_USER_INFO));
        }

        return ResponseEntity.ok()
                .body(user.get().getProfile());
    }

    @ResponseBody
    @PostMapping("/friends/send")
    public ResponseEntity<?> getSendReqs(@RequestBody Map<String, String> emailMap) {
        Optional<User> user = userRepository.findByEmail(emailMap.get("email"));
        if(user.isEmpty()) {
            return ResponseEntity.ok()
                    .body(new BaseException(INVALID_USER_INFO));
        }

        List<ReqFriendDto> sendReqs = user.get().getSentFriendRequests();
        List<SendReqDto> response = new ArrayList<>();

        for (ReqFriendDto r : sendReqs) {
            User recvUser = r.getReceiverId();

            response.add(SendReqDto.builder()
                    .senderId(r.getSenderId().getId())
                    .receiverId(recvUser.getId())
                    .profile(recvUser.getProfile())
                    .name(recvUser.getName())
                    .email(recvUser.getEmail())
                    .address(recvUser.getAddress())
                    .build());
        }

        return ResponseEntity.ok()
                .body(response);
    }

    @ResponseBody
    @PostMapping("/friends/recv")
    public ResponseEntity<?> getRecvReqs(@RequestBody Map<String, String> emailMap) {
        User user = userRepository.findByEmail(emailMap.get("email")).orElseThrow();
        List<ReqFriendDto> recvReqs = user.getReceivedFriendRequests();
        List<SendReqDto> response = new ArrayList<>();

        for (ReqFriendDto r : recvReqs) {
            User sendUser = r.getSenderId();

            response.add(SendReqDto.builder()
                    .senderId(sendUser.getId())
                    .receiverId(r.getReceiverId().getId())
                    .profile(sendUser.getProfile())
                    .name(sendUser.getName())
                    .email(sendUser.getEmail())
                    .address(sendUser.getAddress())
                    .build());
        }

        return ResponseEntity.ok()
                .body(response);
    }
}
