package com.flat.backend.like.controller;

import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.like.dto.req.LikeReqDto;
import com.flat.backend.like.dto.res.LikeResDto;
import com.flat.backend.like.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("")
    public ResponseEntity<BaseResponseDto<LikeResDto>> like(@RequestBody LikeReqDto likeReqDto) {
        return likeService.like(likeReqDto);
    }

}
