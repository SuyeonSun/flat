package com.flat.backend.token;

import com.flat.backend.user.service.UserDetailServiceImpl;
import com.flat.backend.token.entity.Token;
import com.flat.backend.token.repository.TokenRepository;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final String secretKey = "413F4428472B4B6250655368566D5970337336763979244226452948404D6351";
    // private final Long accessExpirationTime = 43200000L; // 12시간
    // private final Long refreshExpirationTime = 604800000L; // 7일
    private final Long accessExpirationTime = Duration.ofMinutes(1).toMillis(); // 1분
    private final Long refreshExpirationTime = Duration.ofMinutes(2).toMillis();  // 2분
    // private final Long refreshExpirationTime = Duration.ofDays(7).toMillis(); // 7일

    private final UserDetailServiceImpl userDetailService;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    // accessToken 생성
    public String createAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessExpirationTime);
        String accessToken;
        accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        return accessToken;
    }

    // refreshToken 생성
    public void createRefreshToken(Authentication authentication, User user) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);
        String refreshToken;
        refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // DB에 저장
        Token token = Token.builder()
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(token);

        user.setToken(token);
        userRepository.save(user);
    }

    public Authentication getAuthentication(String token) {
        String claims = Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        UserDetails userDetails = userDetailService.loadUserByUsername(claims);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // http 헤더로부터 bearer token을 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // accessToken 재발급
    public boolean validateToken(String token) throws Exception {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            throw new Exception();
        }
    }

    // accessToken 재발급
    public String reIssueAccessToken(String refreshToken) {
        // refresh token 검증
        Authentication authentication = getAuthentication(refreshToken);
        String accessToken;
        accessToken = createAccessToken(authentication);
        return accessToken;
    }
}
