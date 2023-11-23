package com.flat.backend.property.service;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.flat.backend.common.BaseException;
import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.like.repository.LikeRepository;
import com.flat.backend.like.repository.entity.Like;
import com.flat.backend.property.dto.req.DetailReqDto;
import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.dto.res.DetailResDto;
import com.flat.backend.property.repository.PropertyRepository;
import com.flat.backend.property.repository.entity.InterestAreaProperty;
import com.flat.backend.property.repository.entity.InterestAreaPropertyRepository;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.flat.backend.common.BaseResponseStatus.INVALID_USER_INFO;
import static com.flat.backend.common.BaseResponseStatus.OK;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PropertyService {
    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final LikeRepository likeRepository;
    private final InterestAreaPropertyRepository interestAreaPropertyRepository;

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

        List<User> allUser = userRepository.findAll();
        for(User u : allUser) {
            if(u.getInterestLat() != null && u.getAddressLng() != null) {
                double dist = Math.sin(deg2rad(Double.parseDouble(property.getLat()))) * Math.sin(deg2rad(Double.parseDouble(u.getInterestLat())))
                        + Math.cos(deg2rad(Double.parseDouble(property.getLat()))) * Math.cos(deg2rad(Double.parseDouble(u.getInterestLat())))
                        * Math.cos(deg2rad(Double.parseDouble(property.getLng()) - Double.parseDouble(u.getInterestLng())));

                dist = Math.acos(dist);
                dist = rad2deg(dist);
                dist = dist * 60 * 1.1515 * 1609.344;

                if(dist <= Double.parseDouble(user.getInterestRadius())*200) {
                    InterestAreaProperty interestAreaProperty = InterestAreaProperty.builder()
                            .email(u.getEmail())
                            .property(property)
                            .build();

                    interestAreaPropertyRepository.save(interestAreaProperty);
                }
            }
        }


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

    public ResponseEntity<BaseResponseDto<List<Property>>> selectLikeList(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BaseException(INVALID_USER_INFO));
        List<Property> result = new ArrayList<>();
        for(Like like : user.getLikes()) {
            result.add(like.getProperty());
        }

        BaseResponseDto<List<Property>> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), result);
        return ResponseEntity.ok().body(baseResponseDto);
    }

    public ResponseEntity<BaseResponseDto<Page<Property>>> selectListAll(Pageable pageable, String searchKeyword, String tradeTypeName, String direction) {
        Page<Property> result = propertyRepository.findAll(pageable, searchKeyword, tradeTypeName, direction);
        BaseResponseDto<Page<Property>> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), result);
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

    public ResponseEntity<BaseResponseDto<List<Property>>> selectMapList (String address, String tradeTypeName) {
        List<Property> propertyList = propertyRepository.findMapList(address, tradeTypeName);
        BaseResponseDto<List<Property>> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), propertyList);
        return ResponseEntity.ok().body(baseResponseDto);
    }

    public ResponseEntity<BaseResponseDto<List<Property>>> selectInterestAreaMapList(String lat, String lng, String radius) {
        List<Property> properties = propertyRepository.findAll();

        List<Property> result = new ArrayList<>();
        for(Property p : properties) {

            double dist = Math.sin(deg2rad(Double.parseDouble(lat))) * Math.sin(deg2rad(Double.parseDouble(p.getLat())))
                    + Math.cos(deg2rad(Double.parseDouble(lat))) * Math.cos(deg2rad(Double.parseDouble(p.getLat())))
                    * Math.cos(deg2rad(Double.parseDouble(lng) - Double.parseDouble(p.getLng())));

            dist = Math.acos(dist);
            dist = rad2deg(dist);
            dist = dist * 60 * 1.1515 * 1609.344;

            if(dist <= Double.parseDouble(radius)*200) {
                result.add(p);
            }
        }

        return ResponseEntity.ok()
                .body(new BaseResponseDto<>(OK.getStatusCode(), OK.getStatusMessage(), result));
    }

    public ResponseEntity<BaseResponseDto<List<Property>>> selectInterestAreaNewProperty(String email) {
        List<InterestAreaProperty> interestAreaProperties = interestAreaPropertyRepository.findByEmail(email);

        List<Property> result = new ArrayList<>();

        for(InterestAreaProperty interestAreaProperty : interestAreaProperties) {
            result.add(interestAreaProperty.getProperty());
        }

        return ResponseEntity.ok()
                .body(new BaseResponseDto<>(OK.getStatusCode(), OK.getStatusMessage(), result));

    }

    @Transactional
    public ResponseEntity<BaseResponseDto<?>> deleteInterestAreaNewProperty(Long[] ids) {
        for(Long id : ids) {
            interestAreaPropertyRepository.deleteByPropertyId(id);
        }

        return ResponseEntity.ok()
                .body(new BaseResponseDto<>(OK.getStatusCode(), OK.getStatusMessage()));
    }

    @Transactional
    public ResponseEntity<BaseResponseDto<?>> deleteInterestAreaAllProperty(String email) {
        interestAreaPropertyRepository.deleteByEmail(email);

        return ResponseEntity.ok()
                .body(new BaseResponseDto<>(OK.getStatusCode(), OK.getStatusMessage()));
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
