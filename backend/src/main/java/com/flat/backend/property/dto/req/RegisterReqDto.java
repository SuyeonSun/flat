package com.flat.backend.property.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter // TODO
@NoArgsConstructor
@AllArgsConstructor
public class RegisterReqDto {
    private String title;
    private String image;
    private String address;
    private String buildingName;
    private String floorInfo;
    private String lat;
    private String lng;
    private String tradeTypeName;
    private String articleFeatureDesc;
    private String tagList;
    private String rentPrc;
    private int roomCnt;
    private int bathroomCnt;
    private String direction;
    private int averageCommonPrice;
    private int averageEtcPrice;
    private int averageHeatPrice;
    private int area1;
    private int area2;
}