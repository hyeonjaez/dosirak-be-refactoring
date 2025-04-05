package com.example.dosirakbe.global.exception;

import com.example.dosirakbe.global.common.api.StatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * packageName    : com.example.dosirakbe.global.exception<br>
 * fileName       : null.java<br>
 * author         : SSAFY<br>
 * date           : 2025-04-04<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * <br>
 * <br>
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseEntity {
    private final StatusEnum statusEnum;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Builder
    private ErrorResponseEntity(StatusEnum statusEnum, HttpStatus httpStatus, String name, String code, String message) {
        this.statusEnum = statusEnum;
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }


    public static ResponseEntity<ErrorResponseEntity> toErrorResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .statusEnum(StatusEnum.FAILURE)
                        .httpStatus(errorCode.getHttpStatus())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponseEntity> toErrorResponseEntity(ErrorCode errorCode, String customMessage) {
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .statusEnum(StatusEnum.FAILURE)
                        .httpStatus(errorCode.getHttpStatus())
                        .code(errorCode.getCode())
                        .message(customMessage)
                        .build());
    }
}
