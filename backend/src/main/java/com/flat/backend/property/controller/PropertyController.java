package com.flat.backend.property.controller;

import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.property.dto.req.DetailReqDto;
import com.flat.backend.property.dto.req.InterestAreaMapReqDto;
import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.dto.res.DetailResDto;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.property.service.PropertyService;
import com.flat.backend.user.dto.req.InterestAreaReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
@Slf4j
public class PropertyController {
    private final PropertyService propertyService;

    // 내가 등록한 매물 등록
    @PostMapping("")
    public ResponseEntity<BaseResponseDto<?>> register(@RequestParam String email, @RequestBody RegisterReqDto registerReqDto) {
        return propertyService.register(email, registerReqDto);
    }

    // 내가 등록한 매물 삭제
    @PostMapping("/{propertyId}")
    public ResponseEntity<BaseResponseDto<?>> delete(@PathVariable Long propertyId, @RequestParam String email) {
        return propertyService.delete(propertyId, email);
    }

    @GetMapping("/like")
    public ResponseEntity<BaseResponseDto<List<Property>>> selectLikeList(@RequestParam String email) {
        return propertyService.selectLikeList(email);
    }

    // 내가 등록한 매물 리스트 조회
    @GetMapping("")
    public ResponseEntity<BaseResponseDto<List<Property>>> selectList(@RequestParam String email) {
        return propertyService.selectList(email);
    }

    // 전체 매물 리스트 조회
    // list?page=0&size=20&keyword="주차"&tradeTypeName="월세"&direction="남향"
    @GetMapping("/list")
    public ResponseEntity<BaseResponseDto<Page<Property>>> selectListAll(Pageable pageable, @RequestParam String searchKeyword, @RequestParam String tradeTypeName, @RequestParam String direction) {
        return propertyService.selectListAll(pageable, searchKeyword, tradeTypeName, direction);
    }

    // 매물 상세 조회
    @GetMapping("/detail/{propertyId}")
    public ResponseEntity<BaseResponseDto<DetailResDto>> selectDetail(@PathVariable Long propertyId, @RequestParam String email) {
        return propertyService.selectDetail(propertyId, email);
    }

    // 지도에 사용할 매물 리스트 조회
    @GetMapping("/map")
    public ResponseEntity<BaseResponseDto<List<Property>>> selectMapList(@RequestParam String address, @RequestParam String tradeTypeName) {
        return propertyService.selectMapList(address, tradeTypeName);
    }

    @PostMapping("/map/interestArea")
    public ResponseEntity<BaseResponseDto<List<Property>>> selectInterestAreaMapList(@RequestBody InterestAreaMapReqDto interestAreaMapReqDto) {
        return propertyService.selectInterestAreaMapList(interestAreaMapReqDto.getLat(), interestAreaMapReqDto.getLng());
    }

    @GetMapping("/list/interestArea")
    public ResponseEntity<BaseResponseDto<List<Property>>> selectInterestAreaNewProperty(@RequestParam String email) {
        return propertyService.selectInterestAreaNewProperty(email);
    }

    @PostMapping("/remove/interestArea")
    public ResponseEntity<BaseResponseDto<?>> deleteInterestAreaNewProperty(@RequestBody Map<String, Long[]> newPropertyMap) {
        return propertyService.deleteInterestAreaNewProperty(newPropertyMap.get("properties"));
    }

    @GetMapping("/remove/interestArea")
    public ResponseEntity<BaseResponseDto<?>> deleteInterestAreaAllProperty(@RequestParam String email) {
        return propertyService.deleteInterestAreaAllProperty(email);
    }
}
