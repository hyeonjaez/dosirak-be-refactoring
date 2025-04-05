package com.example.dosirakbe.global.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

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
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleBusinessException(final CustomException e) {
        return ErrorResponseEntity.toErrorResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseEntity> handleException() {
        return ErrorResponseEntity.toErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseEntity> handleNoResourceFoundException(NoResourceFoundException e) {
        String errorMessages = String.format("[%s] 에 해당하는 엔드포인트를 찾을 수 없습니다.", e.getResourcePath());
        return ErrorResponseEntity.toErrorResponseEntity(ErrorCode.NO_ENDPOINT, errorMessages);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseEntity> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String errorMessages = String.format("[%s] 는 허용되지 않는 메소드입니다.", e.getMethod());
        return ErrorResponseEntity.toErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED, errorMessages);
    }


    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponseEntity> handleValidException(Exception e) {
        String errorMessages;

        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> String.format("[%s](은)는 %s", fieldError.getField(), fieldError.getDefaultMessage()))
                    .reduce((message1, message2) -> message1 + ". " + message2)
                    .orElse("입력값 검증 오류가 발생했습니다.");
        } else if (e instanceof ConstraintViolationException constraintViolationException) {
            errorMessages = constraintViolationException.getConstraintViolations().stream()
                    .map(violation -> String.format("[%s](은)는 %s",
                            violation.getPropertyPath(),
                            violation.getMessage()))
                    .reduce((message1, message2) -> message1 + ". " + message2)
                    .orElse("입력값 검증 오류가 발생했습니다.");
        } else if (e instanceof HttpMessageNotReadableException) {
            errorMessages = "요청 body 를 읽을 수 없습니다. JSON 형식을 확인해주세요.";
        } else {
            errorMessages = "알 수 없는 검증 오류가 발생했습니다.";
        }

        return ErrorResponseEntity.toErrorResponseEntity(ErrorCode.INVALID_INPUT_VALUE, errorMessages);
    }
}
