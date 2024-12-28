package com.example.dosirakbe.global.util;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;


/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : ExceptionEnum<br>
 * author         : femmefatalehaein<br>
 * date           : 10/13/24<br>
 * description    : 애플리케이션에서 발생하는 예외 정보를 정의한 열거형 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/13/24        femmefatalehaein                최초 생성<br>
 */
@Getter
@ToString
public enum ExceptionEnum {

    /**
     * 런타임 예외.
     * <p>HTTP 상태: {@code BAD_REQUEST} (400)</p>
     */
    RUNTIME_EXCEPTION(HttpStatus.BAD_REQUEST, "E0001"),

    /**
     * 접근이 거부된 예외.
     * <p>HTTP 상태: {@code UNAUTHORIZED} (401)</p>
     */
    ACCESS_DENIED_EXCEPTION(HttpStatus.UNAUTHORIZED, "E0002"),

    /**
     * 내부 서버 오류.
     * <p>HTTP 상태: {@code INTERNAL_SERVER_ERROR} (500)</p>
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E0003"),

    /**
     * 리소스 충돌 예외.
     * <p>HTTP 상태: {@code CONFLICT} (409)</p>
     */
    CONFLICT(HttpStatus.CONFLICT, "E0004"),

    /**
     * 보안 관련 예외.
     * <p>HTTP 상태: {@code UNAUTHORIZED} (401)</p>
     */
    SECURITY(HttpStatus.UNAUTHORIZED, "CE0001", "로그인이 필요합니다"),

    /**
     * 데이터가 존재하지 않을 때 발생하는 예외.
     * <p>HTTP 상태: {@code NOT_FOUND} (404)</p>
     */
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "CE0002", "데이터를 찾을 수 없습니다"),

    /**
     * 권한 부족 예외.
     * <p>HTTP 상태: {@code FORBIDDEN} (403)</p>
     */
    PERMISSION_DENIED(HttpStatus.FORBIDDEN, "CE0003", "권한이 없습니다"),

    /**
     * 중복된 이미지 업로드 예외.
     * <p>HTTP 상태: {@code BAD_REQUEST} (400)</p>
     */
    DUPLICATE_IMAGE(HttpStatus.BAD_REQUEST, "CE0004", "중복된 이미지가 업로드되었습니다."),

    /**
     * 이미지가 존재하지 않을 때 발생하는 예외.
     * <p>HTTP 상태: {@code NOT_FOUND} (404)</p>
     */
    NO_IMAGE_EXIST(HttpStatus.NOT_FOUND, "CE0005", "이미지가 존재하지 않습니다."),

    /**
     * 파일 삭제 실패 예외.
     * <p>HTTP 상태: {@code INTERNAL_SERVER_ERROR} (500)</p>
     */
    FAIL_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "CE0006", "파일 삭제에 실패했습니다."),

    /**
     * 파일 업로드 실패 예외.
     * <p>HTTP 상태: {@code INTERNAL_SERVER_ERROR} (500)</p>
     */
    FAIL_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "CE0007", "파일 업로드에 실패했습니다."),

    /**
     * 허용되지 않는 파일 확장자 예외.
     * <p>HTTP 상태: {@code BAD_REQUEST} (400)</p>
     */
    NOT_IMAGE_EXTENSION(HttpStatus.BAD_REQUEST, "CE0008", "허용되지 않는 파일 확장자입니다."),

    /**
     * 잘못된 요청 예외.
     * <p>HTTP 상태: {@code BAD_REQUEST} (400)</p>
     */
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "CE0004", "잘못된 요청입니다"),

    /**
     * 유효하지 않은 Access Token 예외.
     * <p>HTTP 상태: {@code UNAUTHORIZED} (401)</p>
     */
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "CE0009", "유효하지 않은 accessToken입니다."),

    /**
     * 중복된 닉네임 예외.
     * <p>HTTP 상태: {@code BAD_REQUEST} (400)</p>
     */
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "CE00010", "중복된 닉네임입니다");


    private final HttpStatus status;
    private final String code;
    private String message;

    /**
     * 메시지가 없는 {@link ExceptionEnum}을 생성합니다.
     *
     * @param status HTTP 상태 코드
     * @param code   에러 코드
     */
    ExceptionEnum(HttpStatus status, String code) {
        this.status = status;
        this.code = code;
    }

    /**
     * 메시지가 있는 {@link ExceptionEnum}을 생성합니다.
     *
     * @param status  HTTP 상태 코드
     * @param code    에러 코드
     * @param message 기본 메시지
     */
    ExceptionEnum(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
