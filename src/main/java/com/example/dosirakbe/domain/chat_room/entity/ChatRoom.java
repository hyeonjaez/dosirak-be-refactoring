package com.example.dosirakbe.domain.chat_room.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

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

    public ChatRoom(String title) {
        this.title = title;
    }

    @PrePersist
    public void prePersist() {
        if (this.personCount == null) {
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