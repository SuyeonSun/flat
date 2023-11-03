package com.flat.backend.auth.service;

import com.flat.backend.auth.dto.req.ReIssueReqDto;
import com.flat.backend.auth.dto.req.SignInReqDto;
import com.flat.backend.auth.dto.req.SignOutReqDto;
import com.flat.backend.auth.dto.req.SignUpReqDto;
import com.flat.backend.auth.dto.res.ReIssueResDto;
import com.flat.backend.auth.dto.res.SignInResDto;
import com.flat.backend.common.StatusEnum;
import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.token.JwtTokenProvider;
import com.flat.backend.token.entity.Token;
import com.flat.backend.token.repository.TokenRepository;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<BaseResponseDto<?>> signUp(SignUpReqDto signUpDto) {
        User existUser = userRepository.findByEmail(signUpDto.getEmail());
        if (existUser != null) {
            return ResponseEntity
                    .ok()
                    .body(new BaseResponseDto<>(StatusEnum.USER_ALREADY_EXIST_ERROR.getStatusCode(), StatusEnum.USER_ALREADY_EXIST_ERROR.getStatusMessage()));
        }

        User user = User.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .name(signUpDto.getName())
                .profile(signUpDto.getProfile())
                .address(signUpDto.getAddress())
                .addressLat(signUpDto.getAddressLat())
                .addressLng(signUpDto.getAddressLng())
                .build();

        try {
            userRepository.save(user);
            return ResponseEntity
                    .ok()
                    .body(new BaseResponseDto<>(StatusEnum.OK.getStatusCode(), StatusEnum.OK.getStatusMessage()));
        } catch (Exception exception) {
            return ResponseEntity
                    .ok()
                    .body(new BaseResponseDto<>(StatusEnum.INTERNAL_SERVER_ERROR.getStatusCode(), StatusEnum.INTERNAL_SERVER_ERROR.getStatusMessage()));
        }
    }

    @Transactional
    public ResponseEntity<BaseResponseDto<?>> signIn(SignInReqDto signInDto) {
        User user = userRepository.findByEmail(signInDto.getEmail());

        Token token = user.getToken();
        // 로그인 시에는 무조건 서버의 refreshToken 삭제
        if (token != null && token.getRefreshToken() != null) {
            UUID user_token_id = user.getToken().getId();
            user.setToken(null);
            tokenRepository.deleteById(user_token_id);
        }

        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword())
            );
        } catch (Exception exception) {
            BaseResponseDto<SignInResDto> baseResponseDto = new BaseResponseDto<>(StatusEnum.INVALID_USER_INFO.getStatusCode(), StatusEnum.INVALID_USER_INFO.getStatusMessage());
            return ResponseEntity.ok()
                    .body(baseResponseDto);
        }
        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        jwtTokenProvider.createRefreshToken(authentication, user);
        SignInResDto signInResDto = new SignInResDto(user.getEmail(), accessToken);
        BaseResponseDto<SignInResDto> baseResponseDto = new BaseResponseDto<>(StatusEnum.OK.getStatusCode(), StatusEnum.OK.getStatusMessage(), signInResDto);
        return ResponseEntity.ok()
                .body(baseResponseDto);
    }

    public ResponseEntity<BaseResponseDto<?>> reIssue(ReIssueReqDto reIssueDto) {
        User user = userRepository.findByEmail(reIssueDto.getEmail());
        String refreshToken = user.getToken().getRefreshToken();
        String accessToken = jwtTokenProvider.reIssueAccessToken(refreshToken);
        ReIssueResDto reIssueResDto = new ReIssueResDto(accessToken);
        BaseResponseDto<ReIssueResDto> baseResponseDto = new BaseResponseDto<>(StatusEnum.OK.getStatusCode(), StatusEnum.OK.getStatusMessage(), reIssueResDto);
        return ResponseEntity
                .ok()
                .body(baseResponseDto);
    }

    @Transactional
    public ResponseEntity<BaseResponseDto<?>> signOut(SignOutReqDto signOutDto) {
        // refresh token 삭제
        User user = userRepository.findByEmail(signOutDto.getEmail());
        UUID user_token_id = user.getToken().getId();
        user.setToken(null);

        tokenRepository.deleteById(user_token_id);
        BaseResponseDto<ReIssueResDto> baseResponseDto = new BaseResponseDto<>(StatusEnum.OK.getStatusCode(), StatusEnum.OK.getStatusMessage());
        return ResponseEntity
                .ok()
                .body(baseResponseDto);
    }
}
