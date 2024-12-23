package com.example.dosirakbe.domain.elite.repository;

import com.example.dosirakbe.domain.elite.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.elite.repository<br>
 * fileName       : ProblemRepository<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : Problem 엔티티와 관련된 데이터베이스 접근 메서드를 정의한 Repository 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
public interface ProblemRepository extends JpaRepository<Problem, Long> {

    /**
     * 특정 문제 ID로 Problem을 조회합니다.<br>
     *
     * @param problemId 문제의 고유 ID
     * @return 해당 ID에 해당하는 Problem 엔티티
     */
    Problem findByProblemId(Long problemId);

    /**
     * 특정 사용자 ID로 사용자가 아직 풀지 않은 문제를 조회합니다.<br>
     *
     * @param userId 사용자 ID
     * @return 사용자가 풀지 않은 Problem 목록
     */
    @Query("""
    SELECT p FROM Problem p 
    WHERE p.problemId NOT IN (
        SELECT eh.problemId FROM EliteHistory eh WHERE eh.userId = :userId
    )
    """)
    List<Problem> findProblemsNotSolvedByUser(Long userId);
}
