package com.example.dosirakbe.domain.elite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EliteHistoryDto {
    private Long id;
    private Long problemId;
    private Long userId;
    private boolean isCorrect;
}
