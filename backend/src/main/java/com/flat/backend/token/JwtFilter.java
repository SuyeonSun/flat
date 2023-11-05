package com.flat.backend.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flat.backend.auth.dto.res.ReIssueResDto;
import com.flat.backend.common.BaseException;
import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.token.repository.TokenRepository;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static com.flat.backend.common.BaseResponseStatus.EXPIRED_REFRESH_TOKEN;
import static com.flat.backend.common.BaseResponseStatus.INVALID_USER_INFO;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
        } else if (token != null && jwtUtil.isValidAccessToken(token)) { // accessToken 정상
            Authentication authentication = jwtUtil.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } else {
                // accessToken 만료
                String email = jwtUtil.extractUserNameFromExpiredToken(token);
                User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
                String refreshToken = user.getToken().getRefreshToken();

                try {
                    // refreshToken 정상
                    Authentication authentication = jwtUtil.getAuthentication(refreshToken);
                    String accessToken = jwtUtil.createAccessToken(authentication); // accessToken 재발급
                    ObjectNode json = new ObjectMapper().createObjectNode();
                    json.put("code", String.valueOf(BaseResponseStatus.EXPIRED_ACCESS_TOKEN.getStatusCode()));
                    json.put("message", String.valueOf(BaseResponseStatus.EXPIRED_ACCESS_TOKEN.getStatusMessage()));
                    json.put("data", new ReIssueResDto(accessToken).toSomething()); // TODO
                    String newResponse = new ObjectMapper().writeValueAsString(json);
                    response.setContentLength(newResponse.length());
                    response.getOutputStream().write(newResponse.getBytes());
                } catch (ExpiredJwtException expiredJwtException) {
                    // refreshToken 만료
                    UUID user_token_id = user.getToken().getId();
                    user.setToken(null);
                    userRepository.save(user);
                    tokenRepository.deleteById(user_token_id);
                    ObjectNode json = new ObjectMapper().createObjectNode();
                    json.put("code", String.valueOf(BaseResponseStatus.EXPIRED_REFRESH_TOKEN.getStatusCode()));
                    json.put("message", String.valueOf(BaseResponseStatus.EXPIRED_REFRESH_TOKEN.getStatusMessage()));
                    String newResponse = new ObjectMapper().writeValueAsString(json);
                    response.setContentLength(newResponse.length());
                    response.getOutputStream().write(newResponse.getBytes());
                }
        }
    }
}
