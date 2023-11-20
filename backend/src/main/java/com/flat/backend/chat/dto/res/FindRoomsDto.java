package com.flat.backend.chat.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class FindRoomsDto {
    private Long roomId;
    private String sender;
    private String receiver;
    private String title;
    private String image;
}
