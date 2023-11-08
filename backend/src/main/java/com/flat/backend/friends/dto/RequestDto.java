package com.flat.backend.friends.dto;


import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    UUID senderId;
    UUID receiverId;
}
