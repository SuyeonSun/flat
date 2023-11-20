package com.flat.backend.chat.dto.req;

import lombok.Getter;

@Getter
public class MakeRoomDto {
    private String sender;
    private String receiver;
    private Long propertyId;
}
