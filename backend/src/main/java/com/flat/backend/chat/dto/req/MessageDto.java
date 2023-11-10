package com.flat.backend.chat.dto.req;
import com.flat.backend.chat.entity.ChatMessage;
import lombok.Getter;

@Getter
public class MessageDto {

    private ChatMessage.MessageType type;

    private Long roomId;

    //보내는 사람
    private String sender;
    //내용
    private String message;

    private String date;
}
