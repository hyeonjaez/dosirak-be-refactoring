package com.example.dosirakbe.domain.activity_log.entity;

import com.example.dosirakbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "activity_logs")
@EntityListeners(AuditingEntityListener.class)
public class ActivityLog {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "content_id")
    private Long contentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "activity_type")
    @Enumerated(EnumType.STRING)
    private ActivityType activityType;

    @Column(name = "distance")
    private BigDecimal distance;

    public ActivityLog(Long contentId, User user, ActivityType activityType) {
        this.contentId = contentId;
        this.user = user;
        this.activityType = activityType;
    }

    public ActivityLog(Long contentId, User user, ActivityType activityType, BigDecimal distance) {
        this.contentId = contentId;
        this.user = user;
        this.activityType = activityType;
        this.distance = distance;
    }
}
