package com.example.dosirakbe.domain.elite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * packageName    : com.example.dosirakbe.domain.elite.entity<br>
 * fileName       : EliteInfo<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 사용자의 문제 풀이 통계 정보를 저장하는 JPA 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Getter
@Setter
@Entity
@Table(name = "elite_infos") // 테이블명 지정
public class EliteInfo {

    /**
     * 엘리트 정보의 고유 ID입니다.
     * 데이터베이스에서 자동 생성됩니다.
     */
    @Id
    @Column(name = "info_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InfoId;

    /**
     * 사용자의 고유 ID를 나타냅니다.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 사용자가 맞힌 문제 수를 나타냅니다.
     */
    @Column(name = "correct_cnt", nullable = false)
    private int correctAnswers;

    /**
     * 사용자가 틀린 문제 수를 나타냅니다.
     */
    @Column(name = "wrong_cnt", nullable = false)
    private int incorrectAnswers;

    /**
     * 사용자가 마지막으로 문제를 푼 날짜를 나타냅니다.<br>
     * 기본값은 현재 날짜로 설정됩니다.
     */
    @Column(name = "last_solvied_date", nullable = false)
    private LocalDate lastSolvedDate;

    /**
     * 총 풀이한 문제 수를 반환합니다.<br>
     * - 정답 수 + 오답 수를 계산하여 반환.
     *
     * @return 총 풀이 문제 수
     */
    public int getTotalAnswers() {
        return correctAnswers + incorrectAnswers;
    }

    /**
     * 엔티티가 생성되기 전(`@PrePersist`) 마지막으로 푼 날짜를 현재 날짜로 설정합니다.
     */
    @PrePersist
    public void setLastSolvedDate() {
        this.lastSolvedDate = LocalDate.now();
    }
}
