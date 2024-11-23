package com.example.dosirakbe.domain.elite.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EliteHistoryRequestDto {
    private Long userId;      // 사용자 ID
    private Long problemId;   // 문제 ID
    private boolean isCorrect; // 정답 여부
}