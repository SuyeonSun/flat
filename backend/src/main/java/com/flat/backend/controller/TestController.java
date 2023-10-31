package com.flat.backend.controller;

import com.flat.backend.domain.User;
import com.flat.backend.service.AwsS3Service;
import com.flat.backend.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    private final AwsS3Service awsS3Service;

    @PostMapping("/test")
    public void test(@RequestBody User user) {
        System.out.println(user.toString());
        testService.test();
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        return new ResponseEntity<>(awsS3Service.uploadFile(file), HttpStatus.OK);
    }
}
