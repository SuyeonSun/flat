package com.flat.backend.auth.controller;

import com.flat.backend.auth.dto.ReIssueDto;
import com.flat.backend.auth.dto.SignInDto;
import com.flat.backend.auth.dto.SignUpDto;
import com.flat.backend.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody SignUpDto signUpDto) {
        // TODO: 이미 존재하는 user 인지 확인하는 로직이 추가 되어야 한다.
        authService.signUp(signUpDto);
    }

    @PostMapping("/sign-in")
    public String signIn(@RequestBody SignInDto signInDto) throws Exception {
        return authService.signIn(signInDto);
    }

    @PostMapping("/re-issue")
    public String reIssue(@RequestBody ReIssueDto reIssueDto) throws Exception {
        return authService.reIssue(reIssueDto);
    }

    @GetMapping("/resource")
    public String resource() {
        return "This is resource";
    }
}
