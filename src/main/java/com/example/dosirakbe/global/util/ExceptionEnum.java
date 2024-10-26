package com.example.dosirakbe.global.util;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ExceptionEnum {

    // System Exception
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003"),
    CONFLICT(HttpStatus.CONFLICT, "E0004"),
    // Custom Exception
    SECURITY(HttpStatus.UNAUTHORIZED, "CE0001", "로그인이 필요합니다"),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "CE0002", "데이터를 찾을 수 없습니다"),
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "CE0003", "권한이 없습니다"),
<<<<<<< HEAD
    DUPLICATE_IMAGE(HttpStatus.BAD_REQUEST, "CE0004", "중복된 이미지가 업로드되었습니다."),
    NO_IMAGE_EXIST(HttpStatus.NOT_FOUND, "CE0005", "이미지가 존재하지 않습니다."),
    FAIL_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "CE0006", "파일 삭제에 실패했습니다."),
    FAIL_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "CE0007", "파일 업로드에 실패했습니다."),
    NOT_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "CE0008", "허용되지 않는 파일 확장자입니다.");
=======
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "CE0004", "잘못된 요청입니다");
>>>>>>> 07a997392f8bb5585fb155bb4e8ef4b0dc6f96b4


    private final HttpStatus status;
    private final String code;
    private String message;

    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}