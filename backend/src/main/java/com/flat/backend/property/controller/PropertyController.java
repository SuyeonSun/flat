package com.flat.backend.property.controller;

import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    // 매물 등록
    @PostMapping("")
    public void register(@RequestParam String email, @RequestBody RegisterReqDto registerReqDto) {
        propertyService.register(email, registerReqDto);
    }

    // 매물 삭제
    @PostMapping("/{propertyId}")
    public void delete(@PathVariable Long propertyId, @RequestParam String email) {
        propertyService.delete(propertyId, email);
    }

    // 매물 수정

    // 매물 리스트 조회

    // 매물 상세 조회
}
