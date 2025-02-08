package com.example.INFO.domain.user.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionAdvice {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Void> handleUserException(UserException e) {
        return ResponseEntity.status(e.getType().getStatus()).build();
    }
}
