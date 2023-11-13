package com.flat.backend.property.service;

import com.flat.backend.common.BaseException;
import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.like.repository.LikeRepository;
import com.flat.backend.like.repository.entity.Like;
import com.flat.backend.property.dto.req.DetailReqDto;
import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.dto.res.DetailResDto;
import com.flat.backend.property.repository.PropertyRepository;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.flat.backend.common.BaseResponseStatus.INVALID_USER_INFO;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PropertyService {
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final LikeRepository likeRepository;

    public ResponseEntity<BaseResponseDto<?>> register(String email, RegisterReqDto registerReqDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        Property property =  Property.builder()
                .user(user)
                .title(registerReqDto.getTitle())
                .image(registerReqDto.getImage())
                .address(registerReqDto.getAddress())
                .buildingName(registerReqDto.getBuildingName())
                .floorInfo(registerReqDto.getFloorInfo())
                .lat(registerReqDto.getLat())
                .lng(registerReqDto.getLng())
                .tradeTypeName(registerReqDto.getTradeTypeName())
                .articleFeatureDesc(registerReqDto.getArticleFeatureDesc())
                .tagList(registerReqDto.getTagList())
                .rentPrc(registerReqDto.getRentPrc())
                .roomCnt(registerReqDto.getRoomCnt())
                .bathroomCnt(registerReqDto.getBathroomCnt())
                .direction(registerReqDto.getDirection())
                .averageCommonPrice(registerReqDto.getAverageCommonPrice())
                .averageEtcPrice(registerReqDto.getAverageEtcPrice())
                .averageHeatPrice(registerReqDto.getAverageHeatPrice())
                .area1(registerReqDto.getArea1())
                .area2(registerReqDto.getArea2())
                .build();
        propertyRepository.save(property);
        user.getProperties().add(property);
        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage()));
    }

    public ResponseEntity<BaseResponseDto<?>> delete(Long propertyId, String email) {
        propertyRepository.deleteById(propertyId);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        List<Property> propertyList = user.getProperties();
        propertyList.removeIf(property -> property.getId() == propertyId);
        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage()));
    }

    public ResponseEntity<BaseResponseDto<List<Property>>> selectList(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        List<Property> result = new ArrayList<>();
        for (Property property : user.getProperties()) {
            result.add(property);
            // log.info("property = {}", property.getUser().getId());
        }
        BaseResponseDto<List<Property>> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), result);
        return ResponseEntity.ok().body(baseResponseDto);
    }

    public ResponseEntity<BaseResponseDto<List<Property>>> selectListAll() {
        List<Property> result = propertyRepository.findAll();
        BaseResponseDto<List<Property>> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), result);
        return ResponseEntity.ok().body(baseResponseDto);
    }

    public ResponseEntity<BaseResponseDto<DetailResDto>> selectDetail(Long propertyId, String email) {
        Property property = propertyRepository.findById(propertyId).orElseThrow();
        User writer = property.getUser();
        User user = userRepository.findByEmail(email).orElseThrow();

        Like like = likeRepository.findByUserIdAndPropertyId(user.getId(), propertyId);
        Boolean isUserLiked;
        if (like == null) isUserLiked = false;
        else isUserLiked = true;
        List<Like> likes = likeRepository.findByPropertyId(propertyId);
        int likeCount = likes.size();

        DetailResDto detailResDto = DetailResDto.builder()
                .title(property.getTitle())
                .image(property.getImage())
                .address(property.getAddress())
                .buildingName(property.getBuildingName())
                .floorInfo(property.getFloorInfo())
                .lat(property.getLat())
                .lng(property.getLng())
                .tradeTypeName(property.getTradeTypeName())
                .articleFeatureDesc(property.getArticleFeatureDesc())
                .tagList(property.getTagList())
                .rentPrc(property.getRentPrc())
                .roomCnt(property.getRoomCnt())
                .bathroomCnt(property.getBathroomCnt())
                .direction(property.getDirection())
                .averageCommonPrice(property.getAverageCommonPrice())
                .averageEtcPrice(property.getAverageEtcPrice())
                .averageHeatPrice(property.getAverageHeatPrice())
                .area1(property.getArea1())
                .area2(property.getArea2())
                .name(writer.getName())
                .email(writer.getEmail())
                .phoneNumber(writer.getPhoneNumber())
                .isUserLiked(isUserLiked)
                .likeCount(likeCount)
                .build();
        BaseResponseDto<DetailResDto> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), detailResDto);
        return ResponseEntity.ok().body(baseResponseDto);
    }
}
