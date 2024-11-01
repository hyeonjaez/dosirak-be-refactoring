package com.example.dosirakbe.domain.user_activity.entity;

import com.example.dosirakbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_activites")
@EntityListeners(AuditingEntityListener.class)
public class UserActivity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "commit_count")
    private Integer commitCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserActivity(Integer commitCount, User user) {
        this.commitCount = commitCount;
        this.user = user;
    }
}
