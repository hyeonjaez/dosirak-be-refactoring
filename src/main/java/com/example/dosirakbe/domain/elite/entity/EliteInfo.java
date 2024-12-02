package com.example.dosirakbe.domain.elite.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "elite_info") // 테이블명 지정
public class EliteInfo {

    @Id
    @Column(name = "info_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long InfoId;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "correct_cnt", nullable = false)
    private int correctAnswers; // 정답 수

    @Column(name = "wrong_cnt", nullable = false)
    private int incorrectAnswers; // 틀린 정답 수

    @Column(name = "last_date", nullable = false, columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate lastSolvedDate; // 마지막으로 푼 날짜

    // 전체 정답 수 계산 (엔터티 내부에서 계산)
    public int getTotalAnswers() {
        return correctAnswers + incorrectAnswers;
    }
    // 마지막으로 푼 날짜 업데이트
    public void updateLastSolvedDate() {
        this.lastSolvedDate = LocalDate.now();
    }

}
