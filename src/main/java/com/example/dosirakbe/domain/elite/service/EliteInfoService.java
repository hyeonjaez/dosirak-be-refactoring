package com.example.dosirakbe.domain.elite.service;

import com.example.dosirakbe.domain.elite.dto.EliteInfoResponse;
import com.example.dosirakbe.domain.elite.entity.EliteInfo;
import com.example.dosirakbe.domain.elite.entity.QuestionHistory;
import com.example.dosirakbe.domain.elite.repository.EliteInfoRepository;
import com.example.dosirakbe.domain.elite.repository.QuestionHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class EliteInfoService {

    private final EliteInfoRepository eliteInfoRepository;
    private final QuestionHistoryRepository questionHistoryRepository;

    public EliteInfoService(EliteInfoRepository eliteInfoRepository, QuestionHistoryRepository questionHistoryRepository) {
        this.eliteInfoRepository = eliteInfoRepository;
        this.questionHistoryRepository = questionHistoryRepository;
    }


    public EliteInfoResponse getUserStats(Long userId) {
        EliteInfo eliteInfo = eliteInfoRepository.findByUserId(userId);
        if (eliteInfo == null) {
            throw new IllegalArgumentException("해당 userId를 가진 데이터를 찾을 수 없습니다.");
        }

        int totalQuestions = eliteInfo.getCorrectAnsCount() + eliteInfo.getWrongAnsCnt();
        return new EliteInfoResponse(
                eliteInfo.getCorrectAnsCount(),
                eliteInfo.getWrongAnsCnt(),
                totalQuestions,
                eliteInfo.getLastSolvedQuestionId() // 새로운 필드 반환
        );
    }

    // 문제 기록 추가
    public void addQuestionHistory(Long eliteInfoId, Long questionId, boolean isCorrect) {
        EliteInfo eliteInfo = eliteInfoRepository.findById(eliteInfoId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID를 가진 EliteInfo를 찾을 수 없습니다."));

        QuestionHistory history = new QuestionHistory();
        history.setEliteInfo(eliteInfo);
        history.setQuestionId(questionId);
        history.setIsCorrect(isCorrect);

        questionHistoryRepository.save(history);
    }

    // 맞은 문제 조회
//    public List<QuestionHistory> getCorrectQuestions(Long eliteInfoId) {
//        questionHistoryRepository.findByEliteInfo_EliteInfoIdAndIsCorrect(eliteInfoId,true)
//        return questionHistoryRepository.findByEliteInfoIdAndIsCorrect(eliteInfoId, true);
//    }
//
//    // 틀린 문제 조회
//    public List<QuestionHistory> getWrongQuestions(Long eliteInfoId) {
//        return questionHistoryRepository.findByEliteInfoIdAndIsCorrect(eliteInfoId, false);
//    }

    // 문제 풀었을 때 EliteInfo 업데이트

}
