package com.example.INFO.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionType {

    DUPLICATED_USERNAME(HttpStatus.CONFLICT),
    ;

    private final HttpStatus status;
}
