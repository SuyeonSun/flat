package com.flat.backend.service;

import com.flat.backend.domain.Test;
import com.flat.backend.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public void test() {
        Test test = new Test();
        test.setText("test text");
        testRepository.save(test);
    }
}
