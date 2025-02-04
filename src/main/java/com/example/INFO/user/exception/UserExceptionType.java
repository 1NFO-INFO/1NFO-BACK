package com.example.INFO.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionType {

    DUPLICATED_USERNAME(HttpStatus.CONFLICT),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final HttpStatus status;
}
