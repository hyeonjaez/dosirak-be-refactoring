package com.example.dosirakbe.domain.message.entity;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * packageName    : com.example.dosirakbe.domain.message.entity<br>
 * fileName       : Message<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 메시지 정보를 저장하는 JPA 엔티티 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
@Entity
@Getter
@Setter
@Table(name = "messages")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Message extends BaseEntity {

    /**
     * 메시지의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 메시지의 고유 ID를 나타내며, 데이터베이스에서 자동으로 생성됩니다.
     * </p>
     */
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 메시지의 내용입니다.
     *
     * <p>
     * 이 필드는 사용자가 전송한 실제 메시지 내용을 담고 있으며, TEXT 타입으로 저장됩니다.
     * </p>
     */
    @Column(columnDefinition = "TEXT")
    private String content;

    /**
     * 메시지의 유형을 나타냅니다.
     *
     * <p>
     * 이 필드는 {@link MessageType} 열거형을 사용하여 메시지의 종류를 정의하며,
     * {@link MessageTypeConverter}를 통해 데이터베이스에 저장됩니다.
     * </p>
     */
    @Column(name = "type")
    @Convert(converter = MessageTypeConverter.class)
    private MessageType messageType;

    /**
     * 메시지를 보낸 사용자입니다.
     *
     * <p>
     * 이 필드는 {@link User} 엔티티와 다대일 관계를 맺으며,
     * 메시지를 보낸 사용자를 나타냅니다.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 메시지가 속한 채팅 방입니다.
     *
     * <p>
     * 이 필드는 {@link ChatRoom} 엔티티와 다대일 관계를 맺으며,
     * 메시지가 속한 채팅 방을 나타냅니다.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    /**
     * 메시지 생성 시 필요한 필드를 초기화하는 생성자입니다.
     *
     * <p>
     * 이 생성자는 {@link Message} 객체를 생성할 때,
     * {@code content}, {@code messageType}, {@code user}, {@code chatRoom} 필드를 초기화합니다.
     * </p>
     *
     * @param content     메시지의 내용
     * @param messageType 메시지의 유형 {@link MessageType}
     * @param user        메시지를 보낸 사용자 {@link User}
     * @param chatRoom    메시지가 속한 채팅 방 {@link ChatRoom}
     */
    public Message(String content, MessageType messageType, User user, ChatRoom chatRoom) {
        this.content = content;
        this.messageType = messageType;
        this.user = user;
        this.chatRoom = chatRoom;
    }
}
