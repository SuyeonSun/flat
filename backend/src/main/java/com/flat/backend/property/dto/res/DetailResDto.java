package com.flat.backend.property.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DetailResDto {
    private String address;
    private String image;
    private String title;
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

    private String name;
    private String email;
}
