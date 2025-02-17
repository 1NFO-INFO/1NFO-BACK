package com.example.INFO.global.exception.controller;

import com.example.INFO.global.exception.DefaultException;
import com.example.INFO.global.payload.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(DefaultException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ErrorResponse.of(e.getErrorCode()));
    }
}
