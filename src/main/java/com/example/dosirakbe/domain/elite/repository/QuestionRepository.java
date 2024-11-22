package com.example.dosirakbe.domain.elite.repository;

import com.example.dosirakbe.domain.elite.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
