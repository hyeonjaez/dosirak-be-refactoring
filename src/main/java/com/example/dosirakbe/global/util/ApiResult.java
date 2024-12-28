package com.example.dosirakbe.global.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : ApiResult<br>
 * author         : femmefatalehaein<br>
 * date           : 10/13/24<br>
 * description    : API 응답을 캡슐화하는 제네릭 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/13/24        femmefatalehaein                최초 생성<br>
 */
@Getter
@NoArgsConstructor
@ToString
public class ApiResult<T> {

    /**
     * 응답 상태를 나타냅니다.
     *
     * <p>
     * 성공 또는 실패 여부를 나타내는 {@link StatusEnum} 값입니다.
     * </p>
     */
    private StatusEnum status;

    /**
     * 응답 메시지를 나타냅니다.
     *
     * <p>
     * 성공 또는 실패에 대한 설명 메시지를 포함합니다.
     * </p>
     */
    private String message;

    /**
     * 응답 데이터입니다.
     *
     * <p>
     * 성공적인 API 호출 시 반환되는 데이터입니다. 제네릭 타입 {@code T}로 정의되어 있어 다양한 유형의 데이터를 처리할 수 있습니다.
     * </p>
     */
    private T data;

    /**
     * 예외 정보입니다.
     *
     * <p>
     * API 호출 중 발생한 예외의 상세 정보를 포함하는 {@link ApiExceptionEntity} 객체입니다.
     * </p>
     */
    private ApiExceptionEntity exception;

    /**
     * {@link ApiResult} 객체를 생성합니다.
     *
     * <p>
     * 빌더 패턴을 통해 응답 상태, 메시지, 데이터, 예외 정보를 포함한 객체를 생성할 수 있습니다.
     * </p>
     *
     * @param status    응답 상태를 나타내는 {@link StatusEnum}
     * @param message   응답 메시지
     * @param data      응답 데이터
     * @param exception 예외 정보
     */
    @Builder
    public ApiResult(StatusEnum status, String message, T data, ApiExceptionEntity exception) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.exception = exception;
    }

}