package com.example.dosirakbe.domain.elite.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "problem") // 테이블명 지정
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long problemId;


    @Column(name = "problem_desc", nullable = false)
    private String description; // 문제 설명

    @Column(name = "problem_ans", nullable = false)
    private String answer; // 정답
}
