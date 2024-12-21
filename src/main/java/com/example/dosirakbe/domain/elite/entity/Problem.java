package com.example.dosirakbe.domain.elite.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : com.example.dosirakbe.domain.elite.entity<br>
 * fileName       : Problem<br>
 * author         : femmefatlaehaein<br>
 * date           : 11/23/24<br>
 * description    : 문제 정보를 저장하는 JPA 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/23/24        femmefatlaehaein       최초 생성<br>
 */
@Getter
@Setter
@Entity
@Table(name = "problems") // 테이블명 지정
public class Problem {

    /**
     * 문제의 고유 ID입니다.
     * 데이터베이스에서 자동 생성됩니다.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "problem_id")
    private Long problemId;

    /**
     * 문제의 설명을 나타냅니다.
     * 해당 필드는 null 값을 허용하지 않습니다.
     */
    @Column(name = "problem_desc", nullable = false)
    private String description;

    /**
     * 문제의 정답을 나타냅니다.
     * 해당 필드는 null 값을 허용하지 않습니다.
     */
    @Column(name = "problem_ans", nullable = false)
    private String answer;
}
