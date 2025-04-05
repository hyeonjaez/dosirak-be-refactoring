package com.example.dosirakbe.domain;

import com.example.dosirakbe.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DomainErrorCode implements ErrorCode {
    ACTIVITY_LOG_DISTANCE_INVALID_VALUE(HttpStatus.BAD_REQUEST, "ACTIVITY-001", "distance 값의 유효성 검증에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    DomainErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
