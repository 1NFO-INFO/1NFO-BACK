package com.example.INFO.global.exception;

import com.example.INFO.global.payload.ErrorCode;

public class UnauthorizedException extends CustomException {
    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}