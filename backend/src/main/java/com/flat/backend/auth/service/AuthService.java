package com.flat.backend.auth.service;

import com.flat.backend.JwtTokenProvider;
import com.flat.backend.auth.dto.SignInDto;
import com.flat.backend.auth.dto.SignUpDto;
import com.flat.backend.token.entity.Token;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SignUpDto signUpDto) {
        User user = User.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();
        userRepository.save(user);
    }

    public String signIn(SignInDto signInDto) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword())
            );

            String accessToken = jwtTokenProvider.createAccessToken(authentication);
            jwtTokenProvider.createRefreshToken(authentication);
            return accessToken;
        } catch (Exception e) {
            throw e;
        }
    }
}
