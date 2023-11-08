package com.flat.backend.property.service;

import com.flat.backend.common.BaseException;
import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.repository.PropertyRepository;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public void delete(Long propertyId, String email) {
        propertyRepository.deleteById(propertyId);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        List<Property> propertyList = user.getProperties();
        propertyList.removeIf(property -> property.getId() == propertyId);
    }

    public List<Property> selectList(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        List<Property> result = new ArrayList<>();
        for (Property property : user.getProperties()) {
            result.add(property);
            // log.info("property = {}", property.getUser().getId());
        }
        return result;
    }

    public List<Property> selectListAll() {
        return propertyRepository.findAll();
    }

    public Property selectDetail(Long propertyId) {
        return propertyRepository.findById(propertyId).orElseThrow();
    }
}
