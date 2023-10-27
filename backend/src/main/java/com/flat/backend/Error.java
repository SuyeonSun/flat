package com.flat.backend;

import lombok.Getter;

@Getter
public class Error {
    private int code;
    private String message;

    public Error(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
