package com.example.dosirakbe.domain.elite.service;

import com.example.dosirakbe.domain.elite.dto.EliteInfoDto;
import com.example.dosirakbe.domain.elite.repository.EliteInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.elite.service<br>
 * fileName       : EliteInfoService<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 사용자의 엘리트 통계 정보를 관리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Service
@RequiredArgsConstructor
public class EliteInfoService {

    private final EliteInfoRepository eliteInfoRepository;

    /**
     * 특정 사용자 ID로 엘리트 통계 정보를 조회합니다.<br>
     * - 사용자의 정답 수, 오답 수, 총 풀이 문제 수를 포함합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자의 엘리트 통계 정보를 감싸는 Optional<EliteInfoDto> 객체
     */
    public Optional<EliteInfoDto> findEliteInfoByUserId(Long userId) {
        return eliteInfoRepository.findByUserId(userId)
                .map(eliteInfo -> EliteInfoDto.builder()
                        .id(eliteInfo.getInfoId())
                        .userId(eliteInfo.getUserId())
                        .correctAnswers(eliteInfo.getCorrectAnswers())
                        .incorrectAnswers(eliteInfo.getIncorrectAnswers())
                        .totalAnswers(eliteInfo.getTotalAnswers())
                        .build());
    }
}
