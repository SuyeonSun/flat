package com.flat.backend.auth.service;

import com.flat.backend.auth.dto.req.ReIssueReqDto;
import com.flat.backend.auth.dto.req.SignInReqDto;
import com.flat.backend.auth.dto.req.SignOutReqDto;
import com.flat.backend.auth.dto.req.SignUpReqDto;
import com.flat.backend.auth.dto.res.ReIssueResDto;
import com.flat.backend.auth.dto.res.SignInResDto;
import com.flat.backend.common.BaseException;
import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.token.JwtUtil;
import com.flat.backend.token.entity.Token;
import com.flat.backend.token.repository.TokenRepository;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.flat.backend.common.BaseResponseStatus.INVALID_USER_INFO;
import static com.flat.backend.common.BaseResponseStatus.USER_ALREADY_EXIST_ERROR;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public ResponseEntity<BaseResponseDto<?>> signUp(SignUpReqDto signUpDto) {
        boolean exsitUser = userRepository.findByEmail(signUpDto.getEmail()).isPresent();
        if(exsitUser){
            throw new BaseException(USER_ALREADY_EXIST_ERROR);
        }
//                .orElseThrow(() -> new BaseException(USER_ALREADY_EXIST_ERROR)); // USER_ALREADY_EXIST_ERROR
        /*
        if (existUser != null) {
            throw new BaseException(USER_ALREADY_EXIST_ERROR);
        } */

        User user = User.builder()
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .name(signUpDto.getName())
                .profile(signUpDto.getProfile())
                .address(signUpDto.getAddress())
                .addressLat(signUpDto.getAddressLat())
                .addressLng(signUpDto.getAddressLng())
                .phoneNumber(signUpDto.getPhoneNumber())
                .build();

        if(user.getProfile() == null) {
            user.setProfile("https://flat-bucket.s3.ap-northeast-2.amazonaws.com/0ecd56b8-0b4c-4072-92ff-bf4e91c49a21.png");
        }

        userRepository.save(user);
        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage()));

    }

    // 로그인
    @Transactional
    public ResponseEntity<BaseResponseDto<?>> signIn(SignInReqDto signInDto) {
        User user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        Token token = user.getToken();

        // 로그인 시에는 무조건 서버의 refreshToken 삭제
        if (token != null && token.getRefreshToken() != null) {
            UUID user_token_id = user.getToken().getId();
            user.setToken(null);
            tokenRepository.deleteById(user_token_id);
        }

        // accessToken 생성
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInDto.getEmail(), signInDto.getPassword())
        );
        String accessToken = jwtTokenProvider.createAccessToken(authentication);

        // refreshToken 생성
        jwtTokenProvider.createRefreshToken(authentication, user);

        SignInResDto signInResDto = new SignInResDto(user.getEmail(), accessToken, user.getName());
        BaseResponseDto<SignInResDto> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), signInResDto);
        return ResponseEntity.ok()
                .body(baseResponseDto);
    }

    // accessToken 재생성
    public ResponseEntity<BaseResponseDto<?>> reIssue(ReIssueReqDto reIssueDto) {
        User user = userRepository.findByEmail(reIssueDto.getEmail()).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        String refreshToken = user.getToken().getRefreshToken();
        String accessToken = jwtTokenProvider.reIssueAccessToken(refreshToken);

        ReIssueResDto reIssueResDto = new ReIssueResDto(accessToken);
        BaseResponseDto<ReIssueResDto> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), reIssueResDto);
        return ResponseEntity
                .ok()
                .body(baseResponseDto);
    }

    // 로그아웃
    @Transactional
    public ResponseEntity<BaseResponseDto<?>> signOut(SignOutReqDto signOutDto) {
        // refresh token 삭제
        User user = userRepository.findByEmail(signOutDto.getEmail()).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        UUID user_token_id = user.getToken().getId();
        user.setToken(null);
        tokenRepository.deleteById(user_token_id);

        BaseResponseDto<ReIssueResDto> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage());
        return ResponseEntity
                .ok()
                .body(baseResponseDto);
    }
}
