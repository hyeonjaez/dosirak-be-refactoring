package com.example.dosirakbe.domain.user_chat_room.entity;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_chat_room")
@NoArgsConstructor
@AllArgsConstructor
public class UserChatRoom {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "chat_room")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @JoinColumn(name = "users")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    public UserChatRoom(ChatRoom chatRoom, User user) {
        this.chatRoom = chatRoom;
        this.user = user;
    }
}
