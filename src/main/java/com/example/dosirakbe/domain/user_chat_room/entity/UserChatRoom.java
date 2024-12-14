package com.example.dosirakbe.domain.user_chat_room.entity;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * packageName    : com.example.dosirakbe.domain.user_chat_room.entity<br>
 * fileName       : UserChatRoom<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 사용자의 채팅방 참여 기록을 저장하는 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
@Entity
@Getter
@Setter
@Table(name = "user_chat_room")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserChatRoom {

    /**
     * 사용자 채팅방 참여 기록의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 사용자와 채팅방 간의 참여 기록을 유일하게 식별하기 위한 ID를 나타냅니다.
     * </p>
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자가 참여하고 있는 채팅방입니다.
     *
     * <p>
     * 이 필드는 {@link ChatRoom} 엔티티와의 다대일(N:1) 관계를 나타내며, 사용자가 여러 개의 채팅방에 참여할 수 있습니다.
     * 지연 로딩(LAZY) 전략을 사용하여 성능을 최적화합니다.
     * </p>
     */
    @JoinColumn(name = "chat_room")
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    /**
     * 채팅방에 참여한 사용자입니다.
     *
     * <p>
     * 이 필드는 {@link User} 엔티티와의 다대일(N:1) 관계를 나타내며, 사용자가 여러 개의 채팅방에 참여할 수 있습니다.
     * 지연 로딩(LAZY) 전략을 사용하여 성능을 최적화합니다.
     * </p>
     */
    @JoinColumn(name = "users")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 사용자 채팅방 참여가 기록된 시각입니다.
     *
     * <p>
     * 이 필드는 사용자가 채팅방에 참여한 날짜와 시간을 {@link LocalDateTime} 형식으로 나타냅니다.
     * 자동으로 생성되며, 변경되지 않습니다.
     * </p>
     */
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    /**
     * 사용자 채팅방 참여 기록의 생성자입니다.
     *
     * <p>
     * 이 생성자는 참여할 {@link ChatRoom}과 {@link User}를 기반으로 새로운 사용자 채팅방 참여 기록을 생성합니다.
     * </p>
     *
     * @param chatRoom 사용자가 참여할 {@link ChatRoom} 엔티티 객체
     * @param user     사용자를 나타내는 {@link User} 엔티티 객체
     */
    public UserChatRoom(ChatRoom chatRoom, User user) {
        this.chatRoom = chatRoom;
        this.user = user;
    }
}
