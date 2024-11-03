package com.example.dosirakbe.domain.user_activity.entity;

import com.example.dosirakbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Objects;

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

    public UserActivity(User user) {
        this.user = user;
    }

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(this.commitCount)) {
            this.commitCount = 1;
        }
    }

    public void addCommitCount() {
        this.commitCount++;
    }
}
