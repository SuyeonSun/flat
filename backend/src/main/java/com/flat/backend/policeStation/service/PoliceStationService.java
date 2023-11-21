package com.flat.backend.policeStation.service;

import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.policeStation.repository.PoliceStationRepository;
import com.flat.backend.policeStation.repository.entity.PoliceStationLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoliceStationService {
    private final PoliceStationRepository policeStationRepository;
    public ResponseEntity<BaseResponseDto<List<PoliceStationLocation>>> getPoliceStationLocation() {
        List<PoliceStationLocation> policeStationLocations = policeStationRepository.findAll();
        BaseResponseDto<List<PoliceStationLocation>> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), policeStationLocations);
        return ResponseEntity
                .ok()
                .body(baseResponseDto);
    }
}
