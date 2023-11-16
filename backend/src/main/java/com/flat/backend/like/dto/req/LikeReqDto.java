package com.flat.backend.like.dto.req;

import lombok.Getter;

@Getter
public class LikeReqDto {
    private String email;
    private Long propertyId;
    private Boolean isLike;
}
