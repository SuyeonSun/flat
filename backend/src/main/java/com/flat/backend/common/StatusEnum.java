package com.flat.backend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    OK(200, "OK"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR"),

    // user
    USER_ALREADY_EXIST_ERROR(2000, "USER_ALREADY_EXIST_ERROR"),
    INVALID_USER_INFO(2001, "INVALID_USER_INFO"),

    // token
    EXPIRED_ACCESS_TOKEN(2100, "EXPIRED_ACCESS_TOKEN"),
    EXPIRED_REFRESH_TOKEN(2101, "EXPIRED_REFRESH_TOKEN");

    final int statusCode;
    final String statusMessage;
}
