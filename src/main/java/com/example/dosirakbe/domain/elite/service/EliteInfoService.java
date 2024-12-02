package com.example.dosirakbe.domain.elite.service;


import com.example.dosirakbe.domain.elite.dto.EliteInfoDto;
import com.example.dosirakbe.domain.elite.repository.EliteInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EliteInfoService {

    private final EliteInfoRepository eliteInfoRepository;

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
