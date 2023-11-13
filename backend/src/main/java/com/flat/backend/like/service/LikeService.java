package com.flat.backend.like.service;

import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.like.dto.req.LikeReqDto;
import com.flat.backend.like.dto.res.LikeResDto;
import com.flat.backend.like.repository.LikeRepository;
import com.flat.backend.like.repository.entity.Like;
import com.flat.backend.property.repository.PropertyRepository;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    public ResponseEntity<BaseResponseDto<LikeResDto>> like(LikeReqDto likeReqDto) {
        System.out.println("====================================="+ likeReqDto.getEmail());
        Boolean isUserLiked;
        if (likeReqDto.getIsLike()) {
            Property property = propertyRepository.findById(likeReqDto.getPropertyId()).orElseThrow();
            User user = userRepository.findByEmail(likeReqDto.getEmail()).orElseThrow();
            Like like = Like.builder().property(property).user(user).build();
            likeRepository.save(like);
            isUserLiked = true;
        } else {
            User user = userRepository.findByEmail(likeReqDto.getEmail()).orElseThrow();
            Like like = likeRepository.findByUserIdAndPropertyId(user.getId(), likeReqDto.getPropertyId());
            likeRepository.delete(like);
            isUserLiked = false;
        }
        List<Like> likes = likeRepository.findByPropertyId(likeReqDto.getPropertyId());
        int likeCount = likes.size();
        LikeResDto likeResDto = LikeResDto.builder().likeCount(likeCount).isUserLiked(isUserLiked).build();
        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), likeResDto));
    }
}
