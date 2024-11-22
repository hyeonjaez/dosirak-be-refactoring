package com.example.dosirakbe.domain.elite.service;

import com.example.dosirakbe.domain.elite.dto.QuestionDto;
import com.example.dosirakbe.domain.elite.entity.Question;
import com.example.dosirakbe.domain.elite.repository.QuestionRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 모든 문제 조회 (DTO 반환)
    public List<QuestionDto> getAllQuestions() {
        return questionRepository.findAll().stream()
                .map(question -> new QuestionDto(question.getQuestionId(), question.getQuestionText(), question.getAnswer()))
                .collect(Collectors.toList());
    }



}
