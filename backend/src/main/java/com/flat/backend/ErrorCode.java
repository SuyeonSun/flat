package com.flat.backend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ERROR("INVALID_ERROR", "에러 발생");

    private final String code;
    private final String message;
}
