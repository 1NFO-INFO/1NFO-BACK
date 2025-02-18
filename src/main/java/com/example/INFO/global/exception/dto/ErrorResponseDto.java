package com.example.INFO.global.exception.dto;

import com.example.INFO.global.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private final String errorCode;
    private final String message;
    private final String detail;

    public static ErrorResponseDto res(final CustomException customException) {
        return new ErrorResponseDto(
                customException.getErrorCode().getCode(),
                customException.getErrorCode().getMessage(),
                customException.getDetail()
        );
    }

    public static ErrorResponseDto res(final String errorCode, final Exception e) {
        return new ErrorResponseDto(errorCode, e.getMessage(), null);
    }
}