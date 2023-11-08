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
}
