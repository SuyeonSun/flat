package com.flat.backend.property.repository.entity;

import com.flat.backend.user.repository.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "properties")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Property {
    @Id @GeneratedValue
    @Column(name = "property_id")
    private Long id;
    private String address;
    @Column(name = "building_name")
    private String buildingName;
    @Column(name = "floor_info")
    private String floorInfo;
    private String lat;
    private String lng;
    @Column(name = "trade_type_name")
    private String tradeTypeName;
    @Column(name = "article_feature_desc")
    private String articleFeatureDesc;
    @Column(name = "tag_list")
    private String tagList;
    @Column(name = "rent_prc")
    private String rentPrc;
    @Column(name = "room_cnt")
    private int roomCnt;
    @Column(name = "bathroom_cnt")
    private int bathroomCnt;
    @Column(name = "direction")
    private String direction;
    @Column(name = "average_common_price")
    private int averageCommonPrice;
    @Column(name = "average_etc_price")
    private int averageEtcPrice;
    @Column(name = "average_heat_price")
    private int averageHeatPrice;
    private int area1;
    private int area2;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
