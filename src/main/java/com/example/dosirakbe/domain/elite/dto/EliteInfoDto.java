package com.example.dosirakbe.domain.elite.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EliteInfoDto {
    private Long id;
    private Long userId;
    private int correctAnswers;
    private int incorrectAnswers;
    private int totalAnswers;
}
