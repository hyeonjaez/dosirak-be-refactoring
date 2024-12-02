package com.example.dosirakbe.domain.elite.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EliteHistoryResponseDto {
    private Long id;              // EliteHistory의 ID
    private Long problemId;       // 문제 ID
    private Long userId;          // 사용자 ID
    private boolean correct;      // 정답 여부
    private String problemDesc;   // 문제 설명
    private String problemAns;    // 문제 정답
}