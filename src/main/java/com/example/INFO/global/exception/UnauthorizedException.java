package com.example.INFO.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
//@ResponseStatus(HttpStatus.FORBIDDEN) // HTTP 403 Forbidden 응답
public class UnauthorizedException extends RuntimeException {
    private final String errorCode;

    public UnauthorizedException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
