package com.flat.backend.websocket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    public enum MessageType {
        CHAT, JOIN, LEAVE, ERROR,
    }

    @NotNull(message = "메시지 타입은 필수 값입니다.")
    private MessageType type;

    @NotBlank(message = "내용은 필수 값입니다.")
    private String content;

    @NotBlank(message = "송신자는 필수 값입니다.")
    private String sender;


}
