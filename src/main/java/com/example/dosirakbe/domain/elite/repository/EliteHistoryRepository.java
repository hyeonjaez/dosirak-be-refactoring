package com.example.dosirakbe.domain.elite.repository;


import com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto;
import com.example.dosirakbe.domain.elite.entity.EliteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EliteHistoryRepository extends JpaRepository<EliteHistory, Long> {
    // 사용자 ID로 EliteHistory 조회
    List<EliteHistory> findByUserId(Long userId);

    // 문제 ID로 EliteHistory 조회
    List<EliteHistory> findByProblemId(Long problemId);

    // 사용자 ID와 문제 ID로 EliteHistory 조회
    List<EliteHistory> findByUserIdAndProblemId(Long userId, Long problemId);

    @Query("""
    SELECT new com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto(
        eh.historyId, eh.problemId, eh.userId, eh.isCorrect, p.description, p.answer
    )
    FROM EliteHistory eh
    JOIN Problem p ON eh.problemId = p.problemId
    WHERE eh.userId = :userId
""")
    List<EliteHistoryResponseDto> findEliteHistoryWithProblemByUserId(@Param("userId") Long userId);


    @Query("""
    SELECT new com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto(
        eh.historyId, eh.problemId, eh.userId, eh.isCorrect, p.description, p.answer
    )
    FROM EliteHistory eh
    JOIN Problem p ON eh.problemId = p.problemId
    WHERE eh.userId = :userId AND eh.isCorrect = true
""")
    List<EliteHistoryResponseDto> findCorrectProblemsByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT new com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto(
        eh.historyId, eh.problemId, eh.userId, eh.isCorrect, p.description, p.answer
    )
    FROM EliteHistory eh
    JOIN Problem p ON eh.problemId = p.problemId
    WHERE eh.userId = :userId AND eh.isCorrect = false
""")
    List<EliteHistoryResponseDto> findIncorrectProblemsByUserId(@Param("userId") Long userId);


}
