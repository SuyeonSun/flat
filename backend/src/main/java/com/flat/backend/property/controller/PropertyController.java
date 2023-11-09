package com.flat.backend.property.controller;

import com.flat.backend.property.dto.req.RegisterReqDto;
import com.flat.backend.property.dto.res.DetailResDto;
import com.flat.backend.property.repository.entity.Property;
import com.flat.backend.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    // 내가 등록한 매물 등록
    @PostMapping("")
    public void register(@RequestParam String email, @RequestBody RegisterReqDto registerReqDto) {
        propertyService.register(email, registerReqDto);
    }

    // 내가 등록한 매물 삭제
    @PostMapping("/{propertyId}")
    public void delete(@PathVariable Long propertyId, @RequestParam String email) {
        propertyService.delete(propertyId, email);
    }

    // 내가 등록한 매물 리스트 조회
    @GetMapping("")
    public List<Property> selectList(@RequestParam String email) {
        return propertyService.selectList(email);
    }

    // 전체 매물 조회
    @GetMapping("/list")
    public List<Property> selectListAll() {
        return propertyService.selectListAll();
    }

    // 매물 상세 조회
    @GetMapping("/detail/{propertyId}")
    public DetailResDto selectDetail(@PathVariable Long propertyId) {
        return propertyService.selectDetail(propertyId);
    }
}
