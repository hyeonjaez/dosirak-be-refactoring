package com.example.dosirakbe.domain.elite.repository;

import com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto;
import com.example.dosirakbe.domain.elite.entity.EliteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.elite.repository<br>
 * fileName       : EliteHistoryRepository<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : EliteHistory 엔티티와 관련된 데이터베이스 접근 메서드를 정의한 Repository 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
public interface EliteHistoryRepository extends JpaRepository<EliteHistory, Long> {

    /**
     * 특정 사용자 ID로 EliteHistory 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 EliteHistory 목록
     */
    List<EliteHistory> findByUserId(Long userId);

    /**
     * 특정 문제 ID로 EliteHistory 목록을 조회합니다.
     *
     * @param problemId 문제 ID
     * @return 해당 문제와 연관된 EliteHistory 목록
     */
    List<EliteHistory> findByProblemId(Long problemId);

    /**
     * 특정 사용자 ID와 문제 ID로 EliteHistory 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param problemId 문제 ID
     * @return 해당 사용자와 문제에 대한 EliteHistory 목록
     */
    List<EliteHistory> findByUserIdAndProblemId(Long userId, Long problemId);

    /**
     * 특정 사용자 ID로 EliteHistory와 관련 문제 정보를 함께 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자와 관련된 EliteHistoryResponseDto 목록
     */
    @Query("""
    SELECT new com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto(
        eh.historyId, eh.problemId, eh.userId, eh.isCorrect, p.description, p.answer
    )
    FROM EliteHistory eh
    JOIN Problem p ON eh.problemId = p.problemId
    WHERE eh.userId = :userId
    """)
    List<EliteHistoryResponseDto> findEliteHistoryWithProblemByUserId(@Param("userId") Long userId);

    /**
     * 특정 사용자 ID로 정답 처리된 문제들을 EliteHistory와 함께 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 정답 처리된 EliteHistoryResponseDto 목록
     */
    @Query("""
    SELECT new com.example.dosirakbe.domain.elite.dto.EliteHistoryResponseDto(
        eh.historyId, eh.problemId, eh.userId, eh.isCorrect, p.description, p.answer
    )
    FROM EliteHistory eh
    JOIN Problem p ON eh.problemId = p.problemId
    WHERE eh.userId = :userId AND eh.isCorrect = true
    """)
    List<EliteHistoryResponseDto> findCorrectProblemsByUserId(@Param("userId") Long userId);

    /**
     * 특정 사용자 ID로 오답 처리된 문제들을 EliteHistory와 함께 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 오답 처리된 EliteHistoryResponseDto 목록
     */
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
