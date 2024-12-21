package com.example.dosirakbe.domain.elite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

/**
 * packageName    : com.example.dosirakbe.domain.elite.dto<br>
 * fileName       : EliteInfoDto<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 사용자의 엘리트 정보(정답/오답/총 풀이 기록 등)를 담는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Getter
@Builder
@AllArgsConstructor
public class EliteInfoDto {

    /**
     * 엘리트 정보의 고유 ID입니다.
     *
     * <p>
     * ID는 필수 입력 사항이며, null일 수 없습니다.
     * </p>
     *
     * @see NotNull
     */
    @NotNull(message = "ID는 null일 수 없습니다.")
    private Long id;

    /**
     * 사용자의 고유 ID입니다.
     *
     * <p>
     * 사용자 ID는 필수 입력 사항이며, null일 수 없습니다.
     * </p>
     *
     * @see NotNull
     */
    @NotNull(message = "User ID는 null일 수 없습니다.")
    private Long userId;

    /**
     * 사용자가 맞힌 문제의 수입니다.
     *
     * <p>
     * 맞힌 문제의 수는 0 이상이어야 하며, 음수는 허용되지 않습니다.
     * </p>
     *
     * @see Min
     */
    @Min(value = 0, message = "CorrectAnswers는 0 이상이어야 합니다.")
    private int correctAnswers;

    /**
     * 사용자가 틀린 문제의 수입니다.
     *
     * <p>
     * 틀린 문제의 수는 0 이상이어야 하며, 음수는 허용되지 않습니다.
     * </p>
     *
     * @see Min
     */
    @Min(value = 0, message = "IncorrectAnswers는 0 이상이어야 합니다.")
    private int incorrectAnswers;

    /**
     * 사용자가 푼 총 문제의 수입니다.
     *
     * <p>
     * 총 문제의 수는 0 이상이어야 하며, 음수는 허용되지 않습니다.
     * </p>
     *
     * @see Min
     */
    @Min(value = 0, message = "TotalAnswers는 0 이상이어야 합니다.")
    private int totalAnswers;

    /**
     * 사용자가 마지막으로 문제를 푼 날짜입니다.
     *
     * <p>
     * 마지막 풀이 날짜는 필수 입력 사항이며, null일 수 없습니다.
     * </p>
     *
     * @see NotNull
     */
    @NotNull(message = "LastSolvedDate는 null일 수 없습니다.")
    private LocalDateTime lastSolvedDate;

    /**
     * 사용자의 상태 설명입니다.
     *
     * <p>
     * 설명은 선택 사항이며, 최소 1자 이상, 최대 200자 이하로 제한됩니다.
     * </p>
     *
     * @see Size
     */
    @Size(min = 1, max = 200, message = "Description은 1자 이상, 200자 이하이어야 합니다.")
    private String description;
}
