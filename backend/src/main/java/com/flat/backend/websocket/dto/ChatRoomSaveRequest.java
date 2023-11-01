package com.flat.backend.websocket.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomSaveRequest {

    @NotBlank(message = "채팅방 이름은 필수 값입니다.")
    @Length(max = 23, message = "채팅방 이름의 길이는 최대 23자 입니다.")
    private String name;

}
