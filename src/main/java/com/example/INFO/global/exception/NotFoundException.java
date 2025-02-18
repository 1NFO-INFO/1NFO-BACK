package com.example.INFO.global.exception;

import com.example.INFO.global.payload.ErrorCode;

public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}