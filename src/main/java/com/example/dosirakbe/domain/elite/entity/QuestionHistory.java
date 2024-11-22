package com.example.dosirakbe.domain.elite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class QuestionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "elite_info_id", nullable = false)
    private EliteInfo eliteInfo;

    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private Boolean isCorrect; // true: 맞음, false: 틀림
}
