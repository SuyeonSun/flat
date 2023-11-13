package com.flat.backend.like.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LikeResDto {
    private Boolean isUserLiked;
    private int likeCount;
}
