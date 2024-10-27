package com.example.dosirakbe.domain.chat_room.entity;

import com.example.dosirakbe.domain.zone_category.entity.ZoneCategory;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "chat_rooms")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ChatRoom {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "person_count")
    private Long personCount;

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @JoinColumn(name = "zone_category_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ZoneCategory zoneCategory;

    @Column
    private String image;

    public ChatRoom(String title, String explanation, ZoneCategory zoneCategory, String image) {
        this.title = title;
        this.explanation = explanation;
        this.zoneCategory = zoneCategory;
        this.image = image;
    }

    @PrePersist
    public void prePersist() {
        if (Objects.isNull(this.personCount)) {
            this.personCount = 1L;
        }
    }

    public void upPersonCount() {
        this.personCount++;
    }

    public void downPersonCount() {
        this.personCount--;
    }
}