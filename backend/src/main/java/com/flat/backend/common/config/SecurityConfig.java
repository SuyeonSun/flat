package com.flat.backend.common.config;

import com.flat.backend.CorsConfig;
import com.flat.backend.token.JwtSecurityConfig;
import com.flat.backend.token.JwtUtil;
import com.flat.backend.token.repository.TokenRepository;
import com.flat.backend.user.repository.UserRepository;
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
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final CorsConfig corsConfig;

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
                .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource())) // cors 설정
                .authorizeHttpRequests(request -> request.requestMatchers("/auth/sign-up", "/auth/sign-in",
                                "/auth/sign-out", "/user/test", "/user/upload", "/ws/**", "/friend/**", "/geocode/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS));

        httpSecurity.apply(new JwtSecurityConfig(jwtUtil, userRepository, tokenRepository));
        return httpSecurity.build();
    }

}
