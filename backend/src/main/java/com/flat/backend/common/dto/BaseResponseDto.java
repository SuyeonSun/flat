package com.flat.backend.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BaseResponseDto<T> {
    int code;
    String message;
    T data;

    public BaseResponseDto(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponseDto(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
