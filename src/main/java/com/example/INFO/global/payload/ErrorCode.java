package com.example.INFO.global.payload;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_PARAMETER(400, "E001", "잘못된 요청 데이터 입니다."),
    INVALID_REPRESENTATION(400, "E002", "잘못된 표현 입니다."),
    INVALID_FILE_PATH(400, "E003", "잘못된 파일 경로 입니다."),
    INVALID_OPTIONAL_ISPRESENT(400, "E004", "해당 값이 존재하지 않습니다."),
    INVALID_CHECK(400, "E005", "해당 값이 유효하지 않습니다."),
    INVALID_AUTHENTICATION(400, "E006", "잘못된 인증입니다."),
    INVALID_TOKEN(400, "E007", "잘못된 토큰입니다."),
    NOT_FOUND(404, "E008", "해당 데이터를 찾을 수 없습니다."),
    DUPLICATE_ERROR(409, "E009", "중복된 데이터가 존재합니다."),
    UNAUTHORIZED(401, "E010", "권한이 없습니다."),
    EMPTY_FILE_EXCEPTION(400, "E011", "빈 파일입니다."),
    IO_EXCEPTION_ON_IMAGE_UPLOAD(500, "E012", "이미지 업로드 중 IO 예외 발생"),
    IO_EXCEPTION_ON_IMAGE_DELETE(500, "E013", "이미지 삭제 중 IO 예외 발생"),
    NO_FILE_EXTENSION(400, "E014", "파일 확장자가 없습니다."),
    INVALID_FILE_EXTENSION(400, "E015", "잘못된 파일 확장자입니다."),
    PUT_OBJECT_EXCEPTION(500, "E016", "S3 객체 업로드 중 오류 발생");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}