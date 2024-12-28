package com.example.dosirakbe.global.util;


import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : CustomException<br>
 * author         : femmefatalehaein<br>
 * date           : 10/26/24<br>
 * description    : API 호출 중 발생하는 커스텀 예외를 처리하기 위한 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/26/24        femmefatalehaein                최초 생성<br>
 */
@Getter
public class CustomException extends RuntimeException {
    private final ExceptionEnum exceptionEnum;

    /**
     * {@link CustomException} 객체를 생성합니다.
     *
     * <p>
     * {@link ExceptionEnum}을 기반으로 예외 메시지를 설정하며, 해당 예외에 대한 추가 정보를 제공합니다.
     * </p>
     *
     * @param exceptionEnum 발생한 예외와 관련된 {@link ExceptionEnum}
     */
    public CustomException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }

    /**
     * 예외와 관련된 HTTP 상태 코드를 반환합니다.
     *
     * <p>
     * {@link ExceptionEnum}에 정의된 HTTP 상태 코드를 반환하여 클라이언트가 적절한 조치를 취할 수 있도록 합니다.
     * </p>
     *
     * @return {@link HttpStatus} 예외와 관련된 HTTP 상태 코드
     */
    public HttpStatus getStatus() {
        return exceptionEnum.getStatus();
    }

    /**
     * 예외와 관련된 에러 코드를 반환합니다.
     *
     * <p>
     * {@link ExceptionEnum}에 정의된 에러 코드를 반환하여 클라이언트가 에러를 식별할 수 있도록 합니다.
     * </p>
     *
     * @return 에러 코드 문자열
     */
    public String getCode() {
        return exceptionEnum.getCode();
    }
}
