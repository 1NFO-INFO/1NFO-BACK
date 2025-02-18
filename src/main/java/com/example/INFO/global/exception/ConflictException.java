package com.example.INFO.global.exception;

import com.example.INFO.global.payload.ErrorCode;

public class ConflictException extends CustomException {

    public ConflictException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}