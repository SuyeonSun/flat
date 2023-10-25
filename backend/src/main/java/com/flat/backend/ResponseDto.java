package com.flat.backend;

import lombok.Getter;

@Getter
public class ResponseDto<T> {
    boolean success;
    T data;
    Error error;

    public ResponseDto(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public ResponseDto(boolean success, T data, ErrorCode errorCode) {
        this.success = success;
        this.data = data;
        this.error = new Error(errorCode);
    }
}
