package com.example.dosirakbe.global.util;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : RequestValidationFailedException<br>
 * author         : Fiat_lux<br>
 * date           : 10/25/24<br>
 * description    : 요청 데이터 유효성 검사 실패 시 발생하는 커스텀 예외 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        Fiat_lux                최초 생성<br>
 */
@Getter
public class RequestValidationFailedException extends ApiException {

    private final BindingResult bindingResult;

    /**
     * {@link RequestValidationFailedException} 객체를 생성합니다.
     *
     * <p>
     * 주어진 {@link BindingResult}를 기반으로 예외를 생성하며, 기본적으로 {@link ExceptionEnum#RUNTIME_EXCEPTION}을 사용합니다.
     * </p>
     *
     * @param bindingResult 유효성 검사 결과를 포함하는 {@link BindingResult} 객체
     */
    public RequestValidationFailedException(BindingResult bindingResult) {
        super(ExceptionEnum.RUNTIME_EXCEPTION);
        this.bindingResult = bindingResult;
    }

    /**
     * 예외 메시지를 반환합니다.
     *
     * <p>
     * 유효성 검사 실패와 관련된 필드 이름, 거부된 값, 기본 메시지를 포함한 상세 정보를 반환합니다.
     * </p>
     *
     * @return 유효성 검사 실패와 관련된 상세 메시지
     */
    @Override
    public String getMessage() {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
                .map(error ->
                        String.format("Field: %s, Rejected value: %s, Message: %s",
                                error.getField(),
                                error.getRejectedValue(),
                                error.getDefaultMessage()))
                .collect(Collectors.joining("; "));
    }
}