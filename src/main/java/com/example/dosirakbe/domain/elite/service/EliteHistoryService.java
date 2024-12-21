package com.example.dosirakbe.domain.elite.service;

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

/**
 * packageName    : com.example.dosirakbe.domain.elite.service<br>
 * fileName       : EliteHistoryService<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 사용자 문제 풀이 기록 및 통계를 관리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
public class EliteHistoryService {

    private final EliteHistoryRepository eliteHistoryRepository;
    private final EliteInfoRepository eliteInfoRepository;

    /**
     * 특정 사용자의 문제 풀이 기록과 문제 정보를 함께 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자의 문제 풀이 기록과 문제 정보를 포함한 EliteHistoryResponseDto 리스트
     */
    public List<EliteHistoryResponseDto> findEliteHistoryWithProblemByUserId(Long userId) {
        return eliteHistoryRepository.findEliteHistoryWithProblemByUserId(userId);
    }

    /**
     * 특정 사용자가 맞힌 문제들을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 정답 처리된 문제들을 포함한 EliteHistoryResponseDto 리스트
     */
    public List<EliteHistoryResponseDto> findCorrectProblemsByUserId(Long userId) {
        return eliteHistoryRepository.findCorrectProblemsByUserId(userId);
    }

    /**
     * 특정 사용자가 틀린 문제들을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 오답 처리된 문제들을 포함한 EliteHistoryResponseDto 리스트
     */
    public List<EliteHistoryResponseDto> findIncorrectProblemsByUserId(Long userId) {
        return eliteHistoryRepository.findIncorrectProblemsByUserId(userId);
    }

    /**
     * 사용자의 문제 풀이 결과를 기록하고, 통계를 업데이트합니다.<br>
     * - 문제 풀이 기록(EliteHistory) 저장<br>
     * - 사용자 통계(EliteInfo) 업데이트
     *
     * @param userId 사용자 ID
     * @param problemId 문제 ID
     * @param isCorrect 문제 풀이 정답 여부
     */
    @Transactional
    public void recordAnswer(Long userId, Long problemId, boolean isCorrect) {
        // 1. 문제 풀이 기록 저장
        EliteHistory history = EliteHistory.builder()
                .userId(userId)
                .problemId(problemId)
                .isCorrect(isCorrect)
                .build();

        eliteHistoryRepository.save(history);
        LocalDate currentDate = LocalDateTime.now().toLocalDate();

        // 2. 사용자 통계 업데이트
        EliteInfo eliteInfo = eliteInfoRepository.findByUserId(userId)
                .orElseGet(() -> {
                    // 해당 사용자가 없으면 새로 생성
                    EliteInfo newInfo = new EliteInfo();
                    newInfo.setUserId(userId);
                    newInfo.setCorrectAnswers(0);
                    newInfo.setIncorrectAnswers(0);
                    newInfo.setLastSolvedDate(currentDate);
                    return eliteInfoRepository.save(newInfo);
                });

        // 정답 여부에 따라 통계 업데이트
        if (isCorrect) {
            eliteInfo.setCorrectAnswers(eliteInfo.getCorrectAnswers() + 1);
        } else {
            eliteInfo.setIncorrectAnswers(eliteInfo.getIncorrectAnswers() + 1);
        }
        eliteInfo.setLastSolvedDate(currentDate);

        eliteInfoRepository.save(eliteInfo);
    }
}
