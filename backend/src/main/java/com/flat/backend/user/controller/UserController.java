package com.flat.backend.user.controller;

import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> getUser(@RequestBody Map<String, String> emailMap) {
        User user = userRepository.findByEmail(emailMap.get("email")).orElseThrow();
        return ResponseEntity.ok()
                .body(user);
    }
}
