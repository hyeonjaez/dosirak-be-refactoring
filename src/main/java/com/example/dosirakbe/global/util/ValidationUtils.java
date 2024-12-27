package com.example.dosirakbe.global.util;

import org.springframework.validation.BindingResult;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : ValidationUtils<br>
 * author         : Fiat_lux<br>
 * date           : 10/25/24<br>
 * description    : 요청 데이터 유효성 검사를 위한 유틸리티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        Fiat_lux                최초 생성<br>
 */
public class ValidationUtils {

    /**
     * 유효성 검사를 수행하고, 오류가 있는 경우 {@link RequestValidationFailedException}을 발생시킵니다.
     *
     * <p>
     * 유효성 검사 결과를 나타내는 {@link BindingResult} 객체를 검사하여, 오류가 포함되어 있다면
     * {@link RequestValidationFailedException}을 던집니다.
     * </p>
     *
     * @param bindingResult 유효성 검사 결과를 포함하는 {@link BindingResult} 객체
     * @throws RequestValidationFailedException 유효성 검사에 실패한 경우 발생
     */
    public static void validationRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new RequestValidationFailedException(bindingResult);
        }
    }

    /**
     * 유틸리티 클래스의 인스턴스화를 방지하기 위한 private 생성자입니다.
     *
     * <p>
     * 이 클래스는 정적 메서드만 포함하며, 인스턴스화할 필요가 없으므로
     * 기본 생성자를 private 으로 설정하여 사용을 제한합니다.
     * </p>
     */
    private ValidationUtils() {
    }
}
