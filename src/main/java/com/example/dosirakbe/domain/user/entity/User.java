package com.example.dosirakbe.domain.user.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "users")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, updatable = false)
    private Long userId;

    @Column(name = "name", updatable = false, nullable = false)
    private String name;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "email", updatable = false, nullable = false)
    private String email;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "profile_img", nullable = false)
    private String profileImg;

    @Column(name = "user_valid")
    private boolean userValid;

    @Column(name = "reward")
    private Integer reward;

    @Column(name = "track_distance")
    private BigDecimal trackDistance;

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(this.trackDistance)) {
            this.trackDistance = BigDecimal.ZERO;
        }

        if (Objects.isNull(this.reward)) {
            this.reward = 0;
        }
    }

    public void addTrackDistance(BigDecimal moveTrackDistance) {
        if (Objects.nonNull(moveTrackDistance)) {
            this.trackDistance = this.trackDistance.add(moveTrackDistance);
        }
    }
}