package com.example.dosirakbe.domain.elite.repository;

import com.example.dosirakbe.domain.elite.entity.QuestionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionHistoryRepository extends JpaRepository<QuestionHistory, Long> {

   //List<QuestionHistory> findByEliteInfo_eliteInfoIdAndIsCorrect(Long eliteInfoId, Boolean isCorrect);
    List<QuestionHistory> findByEliteInfo_EliteInfoIdAndIsCorrect(Long eliteInfoId, Boolean isCorrect);

}
