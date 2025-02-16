package com.example.INFO.global.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    INVALID_PARAMETER("4000", "잘못된 요청 데이터입니다."),
    INVALID_REPRESENTATION("4001", "잘못된 표현입니다."),
    INVALID_FILE_PATH("4002", "잘못된 파일 경로입니다."),
    INVALID_OPTIONAL_ISPRESENT("4003", "해당 값이 존재하지 않습니다."),
    INVALID_CHECK("4004", "해당 값이 유효하지 않습니다."),
    INVALID_AUTHENTICATION("4005", "잘못된 인증입니다."),
    INVALID_TOKEN("4006", "잘못된 토큰입니다."),
    EMPTY_FILE_EXCEPTION("4007", "빈 파일입니다."),
    NO_FILE_EXTENSION("4008", "파일 확장자가 없습니다."),
    INVALID_FILE_EXTENSION("4009", "잘못된 파일 확장자입니다."),

    // 401 Unauthorized
    UNAUTHORIZED("4010", "권한이 없습니다."),

    // 404 Not Found
    NOT_FOUND("4040", "해당 데이터를 찾을 수 없습니다."),

    // 409 Conflict
    DUPLICATE_ERROR("4090", "중복된 데이터가 존재합니다."),

    // 500 Internal Server Error
    IO_EXCEPTION_ON_IMAGE_UPLOAD("5000", "이미지 업로드 중 IO 예외 발생"),
    IO_EXCEPTION_ON_IMAGE_DELETE("5001", "이미지 삭제 중 IO 예외 발생"),
    PUT_OBJECT_EXCEPTION("5002", "S3 객체 업로드 중 오류 발생");

    private final String code;
    private final String message;
}
