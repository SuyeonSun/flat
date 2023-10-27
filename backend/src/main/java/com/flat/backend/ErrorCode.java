package com.flat.backend;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ERROR(2000, "에러 발생"),

    USERS_DUPLICATED_ERROR(2000, "이미 존재하는 계정입니다.");

    private final int code;
    private final String message;
}
