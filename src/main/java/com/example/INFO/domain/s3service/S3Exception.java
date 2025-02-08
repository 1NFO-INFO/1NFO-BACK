package com.example.INFO.domain.s3service;

import com.example.INFO.global.payload.ErrorCode;
import lombok.Getter;

@Getter
public class S3Exception extends RuntimeException {
    private final ErrorCode errorCode;

    public S3Exception(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}