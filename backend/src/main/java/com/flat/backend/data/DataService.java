package com.flat.backend.data;

import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.repository.PropertyRepository;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.property.repository.entity.Property_temp;
import com.flat.backend.user.repository.UserRepository;
import com.flat.backend.user.repository.entity.User;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void insertImage() {
        String[] imageArr = {
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/c6d48a07-d297-437d-b3ef-82304e67fa27.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/30aa542a-720a-4e30-8416-0c9ab034e2c2.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/3b86cbb1-5af0-4bfd-bc70-c82cc165ce41.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/40bf0f49-2cbd-461b-9d85-572f9eb24104.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/79091236-d4e7-4619-ad5b-07b79bac10fe.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/79bc9570-a22d-4363-a8cf-b99e832ab184.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/2387977b-ab50-4fa6-b503-b8cbb0c16fb1.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/824180e5-dbab-4487-af64-a7fda36fcf87.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/a07abeef-8c24-4c83-a9b2-a28cd384e792.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/41bc9e76-c658-4742-9d16-316d43fe6de0.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/6412ee25-8f35-43d0-8efc-02deeedfaf38.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/db541d09-13db-4621-a7c5-16edab4103c0.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/277cea8c-d3c9-44f3-b98c-e786194621d9.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/b0625f9e-7499-486c-9133-45c5afc85288.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/495a308c-c61f-43a1-af76-98e750917f7f.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/9ee84cba-88cc-42ea-a8ff-7571b71734e0.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/36110c76-7c5f-4360-8321-109d14c337ca.PNG",
                "https://flat-bucket.s3.ap-northeast-2.amazonaws.com/da1fb3a0-38c4-47b9-8586-36226cbfe590.PNG"
        };
        List<Property> propertyList = propertyRepository.findAll();
        Random random = new Random();
        for (int i = 0; i < propertyList.size(); i++) {
            int randomIndex = random.nextInt(imageArr.length);
            Property property = propertyList.get(i);
            property.setImage(imageArr[randomIndex]);
        }
    }
}
