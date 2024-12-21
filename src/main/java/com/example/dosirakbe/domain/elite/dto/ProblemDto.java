package com.example.dosirakbe.domain.elite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * packageName    : com.example.dosirakbe.domain.elite.dto<br>
 * fileName       : ProblemDto<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 문제 정보를 담는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Getter
@Builder
@AllArgsConstructor
public class ProblemDto {

    /**
     * 문제의 고유 ID를 나타냅니다.
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
     * 문제의 설명을 나타냅니다.
     *
     * <p>
     * 설명은 필수 입력 사항이며, 최소 10자 이상, 최대 500자 이하로 제한됩니다.
     * </p>
     *
     * @see NotBlank
     * @see Size
     */
    @NotBlank(message = "문제 설명은 필수 입력 사항입니다.")
    @Size(min = 10, max = 500, message = "문제 설명은 최소 10자 이상, 최대 500자 이하이어야 합니다.")
    private String description;

    /**
     * 문제의 정답을 나타냅니다.
     *
     * <p>
     * 정답은 필수 입력 사항이며, 최소 1자 이상, 최대 100자 이하로 제한됩니다.
     * </p>
     *
     * @see NotBlank
     * @see Size
     */
    @NotBlank(message = "정답은 필수 입력 사항입니다.")
    @Size(min = 1, max = 100, message = "정답은 최소 1자 이상, 최대 100자 이하이어야 합니다.")
    private String answer;
}
