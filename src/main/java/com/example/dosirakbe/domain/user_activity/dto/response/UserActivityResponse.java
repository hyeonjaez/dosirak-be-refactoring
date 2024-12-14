package com.example.dosirakbe.domain.user_activity.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

/**
 * packageName    : com.example.dosirakbe.domain.user_activity.dto.response<br>
 * fileName       : UserActivityResponse<br>
 * author         : Fiat_lux<br>
 * date           : 11/02/24<br>
 * description    : 사용자 활동 요약 정보를 응답하기 위한 DTO(Data Transfer Object) 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/02/24        Fiat_lux                최초 생성<br>
 */
@Getter
@AllArgsConstructor
public class UserActivityResponse {

    /**
     * 활동이 기록된 날짜입니다.
     *
     * <p>
     * 이 필드는 사용자의 활동이 발생한 날짜를 {@link LocalDate} 형식으로 나타냅니다.
     * </p>
     */
    private LocalDate createdAt;

    /**
     * 해당 날짜에 사용자가 수행한 커밋 수입니다.
     *
     * <p>
     * 이 필드는 특정 날짜에 사용자가 수행한 커밋의 총 개수를 {@link Integer} 형식으로 나타냅니다.
     * </p>
     */
    private Integer commitCount;

}
