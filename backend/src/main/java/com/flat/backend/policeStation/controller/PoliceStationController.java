package com.flat.backend.policeStation.controller;

import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.policeStation.repository.entity.PoliceStationLocation;
import com.flat.backend.policeStation.service.PoliceStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/police-station")
public class PoliceStationController {
    private final PoliceStationService policeStationService;

    @GetMapping("")
    public ResponseEntity<BaseResponseDto<List<PoliceStationLocation>>> getPoliceStationLocation() {
        return policeStationService.getPoliceStationLocation();
    }
}
