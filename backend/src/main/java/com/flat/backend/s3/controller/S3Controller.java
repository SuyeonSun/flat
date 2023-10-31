package com.flat.backend.s3.controller;

import com.flat.backend.s3.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class S3Controller {
    private final AwsS3Service awsS3Service;

    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        System.out.println("uploadController");
        log.info("file Upload");
        return new ResponseEntity<>(awsS3Service.uploadFile(file), HttpStatus.OK);
    }
}
