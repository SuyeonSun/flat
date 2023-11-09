package com.flat.backend.friends.dto;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendReqDto {
    UUID senderId;
    UUID receiverId;
    String profile;
    String name;
    String email;
    String address;

}
