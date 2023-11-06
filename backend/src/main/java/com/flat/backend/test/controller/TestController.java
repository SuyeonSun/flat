package com.flat.backend.test.controller;

import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.test.dto.res.TestResDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/resource")
    public ResponseEntity<BaseResponseDto<?>> resource() {
        TestResDto testResDto = new TestResDto("This is resource from the server");
        BaseResponseDto<TestResDto> baseResponseDto = new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), testResDto);
        return ResponseEntity
                .ok()
                .body(baseResponseDto);
    }
}
