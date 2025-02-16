package com.example.INFO.global.payload;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse<T> {
    private final int statusCode;
    private final String errorCode;
    private final String message;
    private final T data;

    public static <T> ErrorResponse<T> res(final HttpStatus status, final ErrorCode errorCode) {
        return new ErrorResponse<>(status.value(), errorCode.getCode(), errorCode.getMessage(), null);
    }

    public static <T> ErrorResponse<T> res(final HttpStatus status, final ErrorCode errorCode, final String customMessage) {
        return new ErrorResponse<>(status.value(), errorCode.getCode(), customMessage, null);
    }

    public static <T> ErrorResponse<T> res(final HttpStatus status, final ErrorCode errorCode, final T data) {
        return new ErrorResponse<>(status.value(), errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static <T> ErrorResponse<T> res(final HttpStatus status, final ErrorCode errorCode, final String customMessage, final T data) {
        return new ErrorResponse<>(status.value(), errorCode.getCode(), customMessage, data);
    }
}
