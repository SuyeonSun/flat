package com.flat.backend.websocket.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomException extends RuntimeException{

    private final ErrorCode errorCode;

    public String getMessage() {
        return errorCode.getDetail();
    }
}
