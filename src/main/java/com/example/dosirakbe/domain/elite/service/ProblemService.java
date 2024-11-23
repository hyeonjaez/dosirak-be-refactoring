package com.example.dosirakbe.domain.elite.service;


import com.example.dosirakbe.domain.elite.dto.ProblemDto;
import com.example.dosirakbe.domain.elite.entity.Problem;
import com.example.dosirakbe.domain.elite.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    // 사용자(userId)가 풀지 않은 문제 랜덤 조회
    public ProblemDto getRandomProblemNotSolvedByUser(Long userId) {
        List<Problem> problems = problemRepository.findProblemsNotSolvedByUser(userId);

        if (problems.isEmpty()) {
            return null; // 풀지 않은 문제가 없을 경우 null 반환
        }

        // 랜덤으로 문제 선택
        Collections.shuffle(problems);
        Problem randomProblem = problems.get(0);

        // DTO로 변환
        return ProblemDto.builder()
                .id(randomProblem.getProblemId())
                .description(randomProblem.getDescription())
                .answer(randomProblem.getAnswer())
                .build();
    }
}
