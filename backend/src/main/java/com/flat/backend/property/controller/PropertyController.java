package com.flat.backend.property.controller;

import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.property.dto.req.DetailReqDto;
import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.dto.res.DetailResDto;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
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

    // 내가 등록한 매물 리스트 조회
    @GetMapping("")
    public ResponseEntity<BaseResponseDto<List<Property>>> selectList(@RequestParam String email) {
        return propertyService.selectList(email);
    }

    // 전체 매물 조회
    // /list?page=0&size=10&sort=propertyId,DESC
    @GetMapping("/list")
    public ResponseEntity<BaseResponseDto<Page<Property>>> selectListAll(Pageable pageable) {
        return propertyService.selectListAll(pageable);
    }

    // 매물 상세 조회
    @GetMapping("/detail/{propertyId}")
    public ResponseEntity<BaseResponseDto<DetailResDto>> selectDetail(@PathVariable Long propertyId, @RequestParam String email) {
        return propertyService.selectDetail(propertyId, email);
    }
}
