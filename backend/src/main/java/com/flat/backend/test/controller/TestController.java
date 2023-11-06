package com.flat.backend.test.controller;

import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.common.dto.BaseResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/resource")
    public ResponseEntity<BaseResponseDto<?>> resource() {
        BaseResponseDto<String> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), "This is resource");
        return ResponseEntity
                .ok()
                .body(baseResponseDto);
    }
}
