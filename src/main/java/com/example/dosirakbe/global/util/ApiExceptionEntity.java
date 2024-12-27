package com.example.dosirakbe.global.util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : ApiExceptionEntity<br>
 * author         : femmefatalehaein<br>
 * date           : 10/13/24<br>
 * description    : API 예외 응답 정보를 담는 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/13/24        femmefatalehaein                최초 생성<br>
 */
@Getter
@ToString
public class ApiExceptionEntity {

    /**
     * 예외와 관련된 에러 코드입니다.
     *
     * <p>
     * API 호출 실패 시 반환되는 고유 에러 코드를 나타냅니다.
     * </p>
     */
    private String errorCode;

    /**
     * 예외와 관련된 에러 메시지입니다.
     *
     * <p>
     * 에러의 상세 원인을 설명하는 메시지를 포함합니다.
     * </p>
     */
    private String errorMessage;

    /**
     * {@link ApiExceptionEntity} 객체를 생성합니다.
     *
     * @param status       HTTP 상태 코드 (현재는 사용되지 않음)
     * @param errorCode    에러 코드
     * @param errorMessage 에러 메시지
     */
    @Builder
    public ApiExceptionEntity(HttpStatus status, String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}