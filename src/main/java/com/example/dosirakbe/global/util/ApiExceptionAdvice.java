package com.example.dosirakbe.global.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : ApiExceptionAdvice<br>
 * author         : femmefatalehaein<br>
 * date           : 10/13/24<br>
 * description    : 전역 예외 처리를 담당하는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/13/24        femmefatalehaein                최초 생성<br>
 */
@RestControllerAdvice
public class ApiExceptionAdvice {

    /**
     * {@link ApiException} 발생 시 호출됩니다.
     *
     * <p>
     * 커스텀 예외인 {@link ApiException}에 대해 적절한 HTTP 상태 코드와 응답 본문을 생성하여 반환합니다.
     * </p>
     *
     * @param request 요청 객체
     * @param e       발생한 {@link ApiException}
     * @return {@link ResponseEntity}로 감싼 {@link ApiResult} 객체
     */
    @ExceptionHandler({ApiException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final ApiException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(e.getError().getCode())
                .errorMessage(e.getError().getMessage())
                .build();

        return ResponseEntity
                .status(e.getError().getStatus())
                .body(ApiResult.builder()
                        .status(StatusEnum.ERROR)
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    /**
     * {@link RuntimeException} 발생 시 호출됩니다.
     *
     * <p>
     * {@link RuntimeException} 예외를 처리하며, 기본적으로 내부 서버 오류로 간주합니다.
     * </p>
     *
     * @param request 요청 객체
     * @param e       발생한 {@link RuntimeException}
     * @return {@link ResponseEntity}로 감싼 {@link ApiResult} 객체
     */
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final RuntimeException e) {
        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ExceptionEnum.RUNTIME_EXCEPTION.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(ExceptionEnum.RUNTIME_EXCEPTION.getStatus())
                .body(ApiResult.builder()
                        .status(StatusEnum.ERROR)
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    /**
     * {@link AccessDeniedException} 발생 시 호출됩니다.
     *
     * <p>
     * 사용자가 권한이 없는 자원에 접근하려고 할 때 발생하는 {@link AccessDeniedException}을 처리합니다.
     * </p>
     *
     * @param request 요청 객체
     * @param e       발생한 {@link AccessDeniedException}
     * @return {@link ResponseEntity}로 감싼 {@link ApiResult} 객체
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final AccessDeniedException e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(ExceptionEnum.ACCESS_DENIED_EXCEPTION.getStatus())
                .body(ApiResult.builder()
                        .status(StatusEnum.ERROR)
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

    /**
     * {@link Exception} 발생 시 호출됩니다.
     *
     * <p>
     * 처리되지 않은 일반적인 예외를 처리하며, 기본적으로 내부 서버 오류로 간주합니다.
     * </p>
     *
     * @param request 요청 객체
     * @param e       발생한 {@link Exception}
     * @return {@link ResponseEntity}로 감싼 {@link ApiResult} 객체
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiResult> exceptionHandler(HttpServletRequest request, final Exception e) {

        ApiExceptionEntity apiExceptionEntity = ApiExceptionEntity.builder()
                .errorCode(ExceptionEnum.INTERNAL_SERVER_ERROR.getCode())
                .errorMessage(e.getMessage())
                .build();

        return ResponseEntity
                .status(ExceptionEnum.INTERNAL_SERVER_ERROR.getStatus())
                .body(ApiResult.builder()
                        .status(StatusEnum.ERROR)
                        .message("")
                        .exception(apiExceptionEntity)
                        .build());
    }

}