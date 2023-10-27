package com.flat.backend.auth.controller;

import com.flat.backend.ResponseDto;
import com.flat.backend.auth.dto.ReIssueDto;
import com.flat.backend.auth.dto.SignInDto;
import com.flat.backend.auth.dto.SignOutDto;
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
    public ResponseDto<Boolean> signUp(@RequestBody SignUpDto signUpDto) {
        return authService.signUp(signUpDto);
    }

    @PostMapping("/sign-in")
    public String signIn(@RequestBody SignInDto signInDto) throws Exception {
        return authService.signIn(signInDto);
    }

    @PostMapping("/sign-out")
    public void signOut(@RequestBody SignOutDto signOutDto) throws Exception {
        authService.signOut(signOutDto);
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
