package com.example.INFO.global.exception;

import com.example.INFO.global.payload.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final int statusCode;
    private final String code;
    private final String message;

    public NotFoundException(String message) {
        this.statusCode = ErrorCode.NOT_FOUND.getStatus();
        this.code = ErrorCode.NOT_FOUND.getCode();
        this.message = message;
    }

    public NotFoundException(String code, String message) {
        super(message);
        this.statusCode = 404;
        this.code = code;
        this.message = message;
    }
}
