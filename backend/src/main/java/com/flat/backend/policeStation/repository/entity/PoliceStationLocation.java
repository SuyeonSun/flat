package com.flat.backend.policeStation.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PoliceStationLocation {
    @Id @GeneratedValue
    private Long id;
    private String lat;
    private String lng;
    private String address;
}
