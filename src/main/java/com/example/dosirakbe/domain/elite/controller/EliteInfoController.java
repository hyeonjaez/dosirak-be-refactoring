package com.example.dosirakbe.domain.elite.controller;

import com.example.dosirakbe.domain.elite.dto.EliteInfoResponse;
import com.example.dosirakbe.domain.elite.entity.QuestionHistory;
import com.example.dosirakbe.domain.elite.service.EliteInfoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/elite-info")
public class EliteInfoController {

    private final EliteInfoService eliteInfoService;

    public EliteInfoController(EliteInfoService eliteInfoService) {
        this.eliteInfoService = eliteInfoService;
    }

    @GetMapping("/{userId}")
    public EliteInfoResponse getUserStats(@PathVariable Long userId) {
        return eliteInfoService.getUserStats(userId);
    }

    // 문제 기록 추가
    @PostMapping
    public void addQuestionHistory(@RequestParam Long eliteInfoId, @RequestParam Long questionId, @RequestParam boolean isCorrect) {
        eliteInfoService.addQuestionHistory(eliteInfoId, questionId, isCorrect);
    }

    // 맞은 문제 조회
//    @GetMapping("/correct/{eliteInfoId}")
//    public List<QuestionHistory> getCorrectQuestions(@PathVariable Long eliteInfoId) {
//        return eliteInfoService.getCorrectQuestions(eliteInfoId);
//    }
//
//    // 틀린 문제 조회
//    @GetMapping("/wrong/{eliteInfoId}")
//    public List<QuestionHistory> getWrongQuestions(@PathVariable Long eliteInfoId) {
//        return eliteInfoService.getWrongQuestions(eliteInfoId);
//    }
}
