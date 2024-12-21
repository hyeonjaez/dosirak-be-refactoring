package com.example.dosirakbe.domain.elite.service;

import com.example.dosirakbe.domain.elite.dto.ProblemDto;
import com.example.dosirakbe.domain.elite.entity.Problem;
import com.example.dosirakbe.domain.elite.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;


/**
 * packageName    : com.example.dosirakbe.domain.elite.service<br>
 * fileName       : ProblemService<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 문제와 관련된 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    /**
     * 특정 사용자 ID로 풀지 않은 문제를 랜덤으로 조회합니다.<br>
     * - 문제 리스트를 가져온 후, 무작위로 하나의 문제를 선택합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자가 풀지 않은 문제를 담은 ProblemDto 객체, 풀지 않은 문제가 없으면 null 반환
     */
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

    /**
     * 특정 문제 ID로 문제를 조회합니다.<br>
     * - 문제 ID에 해당하는 문제를 데이터베이스에서 검색하고 DTO로 변환하여 반환합니다.
     *
     * @param problemId 문제 ID
     * @return 문제 정보를 담은 ProblemDto 객체, 해당 문제가 없으면 null 반환
     */
    public ProblemDto findProblemById(Long problemId) {
        Problem problem = problemRepository.findByProblemId(problemId);
        if (problem == null) {
            return null; // 예외 처리는 Controller에서 수행
        }

        // Problem 엔티티를 DTO로 변환
        return new ProblemDto(
                problem.getProblemId(),
                problem.getDescription(),
                problem.getAnswer()
        );
    }
}
