package com.example.INFO.global.exception;

import com.example.INFO.global.payload.ErrorCode;
import lombok.Getter;

public class DefaultException extends CustomException {
    public DefaultException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DefaultException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}