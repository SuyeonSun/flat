package com.flat.backend.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flat.backend.common.StatusEnum;
import com.flat.backend.common.dto.BaseResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.lang.model.type.ErrorType;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            // refresh token 만료 시, jwtTokenProvider.validateToken(token) == null을 return
            ObjectNode json = new ObjectMapper().createObjectNode();
            // client에서 login 페이지로 보낼 수 있도록 exception 처리
            json.put("code", String.valueOf(StatusEnum.EXPIRED_REFRESH_TOKEN.getStatusCode()));
            json.put("message", String.valueOf(StatusEnum.EXPIRED_REFRESH_TOKEN.getStatusMessage()));
            String newResponse = new ObjectMapper().writeValueAsString(json);
            response.setContentLength(newResponse.length());
            response.getOutputStream().write(newResponse.getBytes());
        }
    }

}
