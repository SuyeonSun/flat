package com.flat.backend.data;

import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.repository.PropertyRepository;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.property.repository.entity.Property_temp;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataService {
    private final PropertyRepository propertyRepository;
    private final PropertyTempRepository propertyTempRepository;
    private final UserRepository userRepository;

    public void insert() {

        log.info("insert()@@@@@@@@@@@@");

        // users 리스트 가지고 오기
        List<User> users = userRepository.findAll();

        Random random = new Random();

        // propertyTemps 반복문 돌리기
        List<Property_temp> propertyTemps = propertyTempRepository.findAll();
        for (int i = 0; i < propertyTemps.size(); i++) {
            int randomIndex = random.nextInt(users.size());
            Property_temp propertyTemp = propertyTemps.get(i);
            Property property = Property.builder()
                    .user(users.get(randomIndex))
                    .title(propertyTemp.getTagList().split(" ")[1] + " " + propertyTemp.getDirection() + " 아파트")
                    .address(propertyTemp.getAddress())
                    .buildingName(propertyTemp.getBuildingName())
                    .floorInfo(propertyTemp.getFloorInfo())
                    .lat(propertyTemp.getLat())
                    .lng(propertyTemp.getLng())
                    .tradeTypeName(propertyTemp.getTradeTypeName())
                    .articleFeatureDesc(propertyTemp.getArticleFeatureDesc())
                    .tagList(propertyTemp.getTagList())
                    .rentPrc(propertyTemp.getRentPrc())
                    .roomCnt(propertyTemp.getRoomCnt())
                    .bathroomCnt(propertyTemp.getBathroomCnt())
                    .direction(propertyTemp.getDirection())
                    .averageCommonPrice(propertyTemp.getAverageCommonPrice())
                    .averageEtcPrice(propertyTemp.getAverageEtcPrice())
                    .averageHeatPrice(propertyTemp.getAverageHeatPrice())
                    .area1(propertyTemp.getArea1())
                    .area2(propertyTemp.getArea2())
                    .build();
            propertyRepository.save(property);
            users.get(randomIndex).getProperties().add(property);
        }
    }
}
