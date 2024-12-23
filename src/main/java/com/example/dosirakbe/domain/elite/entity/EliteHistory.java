package com.example.dosirakbe.domain.elite.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.example.dosirakbe.domain.elite.entity<br>
 * fileName       : EliteHistory<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 사용자의 문제 풀이 기록을 저장하는 JPA 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 보호
@Table(name = "elite_histories") // 테이블명 지정
public class EliteHistory {

    /**
     * 문제 풀이 기록의 고유 ID입니다.
     * 데이터베이스에서 자동 생성됩니다.
     */
    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    /**
     * 문제의 고유 ID입니다.
     */
    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    /**
     * 문제를 푼 사용자의 고유 ID입니다.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 문제 풀이의 정답 여부를 나타냅니다.<br>
     * - `true`: 정답<br>
     * - `false`: 오답
     */
    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    /**
     * 객체 생성을 위한 생성자입니다.<br>
     * 빌더 패턴을 사용하여 객체를 안전하고 명확하게 생성할 수 있습니다.
     *
     * @param problemId 문제의 고유 ID
     * @param userId 사용자의 고유 ID
     * @param isCorrect 정답 여부
     */
    @Builder
    public EliteHistory(Long problemId, Long userId, boolean isCorrect) {
        this.problemId = problemId;
        this.userId = userId;
        this.isCorrect = isCorrect;
    }
}
