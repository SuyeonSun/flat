package com.flat.backend.common.config;

import com.flat.backend.token.JwtSecurityConfig;
import com.flat.backend.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request.requestMatchers("/auth/sign-up", "/auth/sign-in",
//                                "/auth/re-issue", "/auth/sign-out", "/user/test", "/user/upload", "/ws/chat",
//                                "/api/v1/chatRooms", "/chat/**", "/favicon.ico")
//                        .permitAll().anyRequest().authenticated())
                .authorizeHttpRequests(request -> request.anyRequest().permitAll())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));
        httpSecurity.apply(new JwtSecurityConfig(jwtTokenProvider));
        return httpSecurity.build();
    }
}
