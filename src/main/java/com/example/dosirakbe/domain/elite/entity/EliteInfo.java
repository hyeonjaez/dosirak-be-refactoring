package com.example.dosirakbe.domain.elite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "elite_info")
public class EliteInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eliteInfoId;

    @Column(nullable = false)
    private int correctAnsCount;

    @Column(nullable = false)
    private int wrongAnsCnt;

    @Column(nullable = false)
    private Long userId;

    @Column
    private Long lastSolvedQuestionId; // 마지막으로 푼 문제 ID 추가
}
