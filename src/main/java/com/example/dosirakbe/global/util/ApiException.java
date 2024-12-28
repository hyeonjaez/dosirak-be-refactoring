package com.example.dosirakbe.global.util;

import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : ApiException<br>
 * author         : femmefatalehaein<br>
 * date           : 10/13/24<br>
 * description    : API 호출 중 발생하는 예외를 처리하기 위한 커스텀 예외 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/13/24        femmefatalehaein                최초 생성<br>
 */
@Getter
public class ApiException extends RuntimeException {
    private ExceptionEnum error;

    /**
     * 지정된 {@link ExceptionEnum}을 기반으로 커스텀 예외를 생성합니다.
     *
     * <p>
     * 예외 메시지는 {@link ExceptionEnum#getMessage()}를 통해 설정됩니다.
     * 이를 통해 예외가 발생했을 때, 해당 예외와 관련된 상세 메시지를 제공합니다.
     * </p>
     *
     * @param e 발생한 예외와 연관된 {@link ExceptionEnum}
     */
    public ApiException(ExceptionEnum e) {
        super(e.getMessage());
        this.error = e;
    }
}