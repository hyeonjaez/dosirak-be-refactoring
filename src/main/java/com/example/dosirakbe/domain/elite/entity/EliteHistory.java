package com.example.dosirakbe.domain.elite.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 보호
@Table(name = "elite_history") // 테이블명 지정
public class EliteHistory {

    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(name = "problem_id", nullable = false)
    private Long problemId; // 문제 ID

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 ID

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect; // 정답 여부

    // 빌더를 통한 객체 생성
    @Builder
    public EliteHistory(Long problemId, Long userId, boolean isCorrect) {
        this.problemId = problemId;
        this.userId = userId;
        this.isCorrect = isCorrect;
    }
}
