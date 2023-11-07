package com.flat.backend.auth.controller;

import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.auth.dto.req.SignInReqDto;
import com.flat.backend.auth.dto.req.SignOutReqDto;
import com.flat.backend.auth.dto.req.SignUpReqDto;
import com.flat.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponseDto<?>> signUp(@RequestBody SignUpReqDto signUpDto) {
        return authService.signUp(signUpDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<BaseResponseDto<?>> signIn(@RequestBody SignInReqDto signInDto) {
        return authService.signIn(signInDto);
    }

    @PostMapping("/sign-out")
    public void signOut(@RequestBody SignOutReqDto signOutDto) {
        authService.signOut(signOutDto);
    }
}
