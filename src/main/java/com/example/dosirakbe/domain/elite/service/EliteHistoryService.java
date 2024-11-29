package com.example.dosirakbe.domain.elite.service;


import com.example.dosirakbe.domain.elite.dto.EliteHistoryDto;
import com.example.dosirakbe.domain.elite.dto.EliteHistoryRequestDto;
import com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto;
import com.example.dosirakbe.domain.elite.entity.EliteHistory;
import com.example.dosirakbe.domain.elite.entity.EliteInfo;
import com.example.dosirakbe.domain.elite.repository.EliteHistoryRepository;
import com.example.dosirakbe.domain.elite.repository.EliteInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EliteHistoryService {

    private final EliteHistoryRepository eliteHistoryRepository;
    private final EliteInfoRepository eliteInfoRepository;
    public List<EliteHistoryDto> findEliteHistoryByUserId(Long userId) {
        List<EliteHistory> histories = eliteHistoryRepository.findByUserId(userId);
        return histories.stream()
                .map(history -> EliteHistoryDto.builder()
                        .id(history.getHistoryId())
                        .problemId(history.getProblemId())
                        .userId(history.getUserId())
                        .isCorrect(history.isCorrect())
                        .build())
                .collect(Collectors.toList());
    }

    public List<EliteHistoryResponseDto> findEliteHistoryWithProblemByUserId(Long userId) {
        return eliteHistoryRepository.findEliteHistoryWithProblemByUserId(userId);
    }

    // 맞은 문제 조회
    public List<EliteHistoryResponseDto> findCorrectProblemsByUserId(Long userId) {
        return eliteHistoryRepository.findCorrectProblemsByUserId(userId);
    }

    // 틀린 문제 조회
    public List<EliteHistoryResponseDto> findIncorrectProblemsByUserId(Long userId) {
        return eliteHistoryRepository.findIncorrectProblemsByUserId(userId);
    }

    @Transactional
    public void recordAnswer(Long userId, Long problemId, boolean isCorrect) {
        // 1. EliteHistory 저장
        EliteHistory history = EliteHistory.builder()
                .userId((long)1)
                .problemId(problemId)
                .isCorrect(isCorrect)
                .build();

        eliteHistoryRepository.save(history);
        LocalDate currentDate = LocalDateTime.now().toLocalDate();

        // 2. EliteInfo 업데이트
        EliteInfo eliteInfo = eliteInfoRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // 해당 사용자가 없으면 새로 생성
                    EliteInfo newInfo = new EliteInfo();
                    newInfo.setUserId((long)1);
                    newInfo.setCorrectAnswers(0);
                    newInfo.setIncorrectAnswers(0);
                    newInfo.setLastSolvedDate(currentDate);
                    return eliteInfoRepository.save(newInfo);
                });

        // 정답 여부에 따라 값 업데이트
        if (isCorrect) {
            eliteInfo.setCorrectAnswers(eliteInfo.getCorrectAnswers() + 1);
            eliteInfo.setLastSolvedDate(currentDate);
        } else {
            eliteInfo.setIncorrectAnswers(eliteInfo.getIncorrectAnswers() + 1);
            eliteInfo.setLastSolvedDate(currentDate);
        }

        eliteInfoRepository.save(eliteInfo);
    }
}
