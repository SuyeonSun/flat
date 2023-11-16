package com.flat.backend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseResponseStatus {
    // basic
    OK(200, "OK"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

    // user
    USER_ALREADY_EXIST_ERROR(2000, "USER_ALREADY_EXIST_ERROR"),
    INVALID_USER_INFO(2001, "INVALID_USER_INFO"),
    INVALID_USER_PASSWORD(2002, "INVALID_USER_PASSWORD"),
    SAME_PASSWORD_ERROR(2003, "SAME_PASSWORD_ERROR"),

    // token
    EXPIRED_ACCESS_TOKEN(2100, "EXPIRED_ACCESS_TOKEN"),
    EXPIRED_REFRESH_TOKEN(2101, "EXPIRED_REFRESH_TOKEN");

    final int statusCode;
    final String statusMessage;
}
