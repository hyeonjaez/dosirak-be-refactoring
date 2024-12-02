package com.example.dosirakbe.domain.elite.repository;



import com.example.dosirakbe.domain.elite.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProblemRepository extends JpaRepository<Problem, Long> {


    // ProblemId로 문제 조회
    Problem findByProblemId(Long problemId);

    // 사용자(userId)가 풀지 않은 문제 조회
    @Query("""
    SELECT p FROM Problem p 
    WHERE p.problemId NOT IN (
        SELECT eh.problemId FROM EliteHistory eh WHERE eh.userId = :userId
    )
""")
    List<Problem> findProblemsNotSolvedByUser(Long userId);

}