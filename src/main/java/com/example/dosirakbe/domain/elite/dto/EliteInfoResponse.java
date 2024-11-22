package com.example.dosirakbe.domain.elite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EliteInfoResponse {
    private int correctAnsCount;
    private int wrongAnsCnt;
    private int totalQuestions;
    private Long lastSolvedQuestionId; // 새로운 필드 추가
}
