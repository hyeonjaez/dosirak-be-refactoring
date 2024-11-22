package com.example.dosirakbe.domain.elite.controller;

import com.example.dosirakbe.domain.elite.dto.QuestionDto;
import com.example.dosirakbe.domain.elite.entity.Question;
import com.example.dosirakbe.domain.elite.service.QuestionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // 모든 문제 조회
    @GetMapping
    public List<QuestionDto> getAllQuestions() {
        return questionService.getAllQuestions();
    }

}
