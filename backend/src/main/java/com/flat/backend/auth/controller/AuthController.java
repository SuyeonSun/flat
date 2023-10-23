package com.flat.backend.auth.controller;

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
        authService.signUp(signUpDto);
    }

    // @ResponseBody
    @PostMapping("/sign-in")
    public String signIn(@RequestBody SignInDto signInDto) throws Exception {
        System.out.println("signInDto = " + signInDto.toString());
        return authService.signIn(signInDto);
    }
}
