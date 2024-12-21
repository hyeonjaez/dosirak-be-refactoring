package com.example.dosirakbe.domain.elite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.example.dosirakbe.domain.elite.dto<br>
 * fileName       : EliteHistoryResponseDto<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 사용자의 문제 풀이 기록 응답 데이터를 담는 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Getter
@Setter
@AllArgsConstructor
public class EliteHistoryResponseDto {

    /**
     * EliteHistory의 고유 ID를 나타냅니다.
     */
    private Long id;

    /**
     * 문제의 고유 ID를 나타냅니다.
     */
    private Long problemId;

    /**
     * 문제를 푼 사용자의 ID를 나타냅니다.
     */
    private Long userId;

    /**
     * 문제 풀이의 정답 여부를 나타냅니다.<br>
     * - `true`: 정답<br>
     * - `false`: 오답
     */
    private boolean correct;

    /**
     * 문제에 대한 설명을 나타냅니다.
     */
    private String problemDesc;

    /**
     * 문제의 정답을 나타냅니다.
     */
    private String problemAns;
}
