package com.example.dosirakbe.global.util;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : StatusEnum<br>
 * author         : femmefatalehaein<br>
 * date           : 10/20/24<br>
 * description    : API 응답 상태를 정의하는 열거형 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        femmefatalehaein                최초 생성<br>
 */
public enum StatusEnum {
    /**
     * 요청이 성공적으로 처리되었음을 나타냅니다.
     */
    SUCCESS,

    /**
     * 요청이 실패했음을 나타냅니다.
     */
    FAILURE,

    /**
     * 요청 처리 중 에러가 발생했음을 나타냅니다.
     */
    ERROR
}
