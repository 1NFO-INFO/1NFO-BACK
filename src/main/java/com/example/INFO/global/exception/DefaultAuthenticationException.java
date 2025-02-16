package com.example.INFO.global.exception;

import com.example.INFO.global.payload.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

public class DefaultAuthenticationException extends CustomException {
    public DefaultAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DefaultAuthenticationException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}