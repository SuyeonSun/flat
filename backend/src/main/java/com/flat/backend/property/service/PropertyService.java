package com.flat.backend.property.service;

import com.flat.backend.common.BaseException;
import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.repository.PropertyRepository;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.flat.backend.common.BaseResponseStatus.INVALID_USER_INFO;

@Service
@RequiredArgsConstructor
@Transactional
public class PropertyService {
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    public void register(String email, RegisterReqDto registerReqDto) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        Property property =  Property.builder()
                .user(user)
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
    }
}
