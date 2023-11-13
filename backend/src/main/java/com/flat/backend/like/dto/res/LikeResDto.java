package com.flat.backend.like.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeResDto {
    private Boolean isUserLiked;
    private int likeCount;
}
