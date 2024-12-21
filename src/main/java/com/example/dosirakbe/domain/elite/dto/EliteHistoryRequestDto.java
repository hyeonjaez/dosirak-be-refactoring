package com.example.dosirakbe.domain.elite.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.example.dosirakbe.domain.elite.dto<br>
 * fileName       : EliteHistoryRequestDto<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 사용자의 문제 풀이 기록 요청 데이터를 담는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Getter
@Setter
public class EliteHistoryRequestDto {

    /**
     * 문제 ID를 나타냅니다.
     *
     * <p>
     * 문제 ID는 필수 입력 사항이며, null일 수 없습니다.
     * </p>
     *
     * @see NotNull
     */
    @NotNull(message = "문제 ID는 null일 수 없습니다.")
    private Long problemId;

    /**
     * 문제 풀이의 정답 여부를 나타냅니다.<br>
     * - `true`: 정답<br>
     * - `false`: 오답<br>
     *
     * <p>
     * 정답 여부는 필수 입력 사항입니다.
     * </p>
     */
    @NotNull(message = "정답 여부는 null일 수 없습니다.")
    private Boolean isCorrect;
}
