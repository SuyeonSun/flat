package com.flat.backend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusEnum {
    OK(200, "OK"),
    INTERNAL_SERER_ERROR(500, "INTERNAL_SERVER_ERROR");

    final int statusCode;
    final String statusMessage;
}
