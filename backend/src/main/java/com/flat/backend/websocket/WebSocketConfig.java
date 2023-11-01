package com.flat.backend.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

//@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker // WebSocket 서버를 활성화 하는데 사용
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // sockJS Fallback을 이용해 노출할 endpoint 설정
    // 특정 주체를 구독하는 사용자에게만 메시지를 보내는 방법아나, 특정 사용자에게 메시지를 보내는 방법을 위해 STOMP 프로토콜 사용
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS(); // 웹소켓을 지원하지 않는 부라우저에 대한 대체 옵션 활성화
    }

    // 메세지 브로커에 관한 설정
    // 한 클라이언트에서 다른 클라이언트로 메시지를 라우팅하는 데 사용되는 메시지 브로커 구성
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 서버 -> 클라이언트로 발행하는 메세지에 대한 endpoint 설정 : 구독
        registry.enableSimpleBroker("/sub");

        // 클라이언트 -> 서버로 발행하는 메세지에 대한 endpoint 설정 : 구독에 대한 메세지
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
