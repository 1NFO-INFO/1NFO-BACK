package com.example.INFO.global.exception.controller;

import com.example.INFO.global.exception.CustomException;
import com.example.INFO.global.exception.dto.ErrorResponseDto;
import com.example.INFO.global.payload.ErrorCode;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomException(CustomException customException) {
        writeLog(customException);
        HttpStatus httpStatus = resolveHttpStatus(customException);
        return new ResponseEntity<>(ErrorResponseDto.res(customException), httpStatus);
    }

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        if (fieldError == null) {
            return new ResponseEntity<>(ErrorResponseDto.res(String.valueOf(HttpStatus.BAD_REQUEST.value()), exception),
                    HttpStatus.BAD_REQUEST);
        }
        ErrorCode validationErrorCode = ErrorCode.INVALID_PARAMETER;
        String detail = fieldError.getDefaultMessage();
        CustomException validationException = new CustomException(validationErrorCode, detail);
        writeLog(validationException);
        return new ResponseEntity<>(ErrorResponseDto.res(validationException), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException exception) {
        writeLog(exception);
        return new ResponseEntity<>(ErrorResponseDto.res(String.valueOf(HttpStatus.NOT_FOUND.value()), exception),
                HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
        writeLog(exception);
        return new ResponseEntity<>(ErrorResponseDto.res(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void writeLog(CustomException exception) {
        log.error("[{}] {}: {}", exception.getClass().getSimpleName(), exception.getErrorCode().getMessage(), exception.getDetail());
    }

    private void writeLog(Exception exception) {
        log.error("[{}]: {}", exception.getClass().getSimpleName(), exception.getMessage());
    }

    private HttpStatus resolveHttpStatus(CustomException exception) {
        return HttpStatus.resolve(Integer.parseInt(exception.getErrorCode().getCode().substring(0, 3)));
    }
}