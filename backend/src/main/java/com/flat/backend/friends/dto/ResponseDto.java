package com.flat.backend.friends.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    UUID senderId;
    UUID receiverId;
}
