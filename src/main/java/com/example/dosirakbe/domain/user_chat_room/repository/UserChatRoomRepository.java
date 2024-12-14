package com.example.dosirakbe.domain.user_chat_room.repository;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user_chat_room.entity.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.user_chat_room.repository<br>
 * fileName       : UserChatRoomRepository<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 사용자 채팅방 참여 기록에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    /**
     * 특정 사용자가 참여하고 있는 모든 채팅방 기록을 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}에 속한 모든 {@link UserChatRoom} 기록을 조회하여 반환합니다.
     * </p>
     *
     * @param user 활동 기록을 조회할 {@link User} 엔티티 객체
     * @return 사용자가 참여하고 있는 모든 {@link UserChatRoom} 기록을 포함하는 {@link List} 형태의 객체 리스트
     */
    List<UserChatRoom> findAllByUser(User user);

    /**
     * 특정 사용자와 특정 채팅방에 대한 참여 기록을 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}와 {@code chatRoom}을 기반으로 해당 사용자의 특정 채팅방에 대한
     * {@link UserChatRoom} 기록을 조회하며, 존재할 경우 {@link Optional}로 감싸서 반환합니다.
     * 존재하지 않을 경우 빈 {@link Optional}을 반환합니다.
     * </p>
     *
     * @param user     활동 기록을 조회할 {@link User} 엔티티 객체
     * @param chatRoom 참여 기록을 조회할 {@link ChatRoom} 엔티티 객체
     * @return 특정 사용자와 채팅방에 대한 참여 기록을 포함하는 {@link Optional} 형태의 {@link UserChatRoom} 객체
     */
    Optional<UserChatRoom> findByUserAndChatRoom(User user, ChatRoom chatRoom);

    /**
     * 특정 사용자와 특정 채팅방에 대한 참여 기록의 존재 여부를 확인합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}와 {@code chatRoom}에 해당하는 {@link UserChatRoom} 기록이
     * 존재하는지 여부를 확인하여 {@code true} 또는 {@code false}를 반환합니다.
     * </p>
     *
     * @param user     활동 기록의 존재 여부를 확인할 {@link User} 엔티티 객체
     * @param chatRoom 참여 기록의 존재 여부를 확인할 {@link ChatRoom} 엔티티 객체
     * @return 주어진 사용자와 채팅방에 대한 참여 기록이 존재할 경우 {@code true}, 그렇지 않을 경우 {@code false}
     */
    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);

    /**
     * 특정 채팅방에 참여하고 있는 모든 사용자 기록을 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code chatRoom}에 속한 모든 {@link UserChatRoom} 기록을 조회하여 반환합니다.
     * </p>
     *
     * @param chatRoom 참여 기록을 조회할 {@link ChatRoom} 엔티티 객체
     * @return 특정 채팅방에 참여하고 있는 모든 {@link UserChatRoom} 기록을 포함하는 {@link List} 형태의 객체 리스트
     */
    List<UserChatRoom> findAllByChatRoom(ChatRoom chatRoom);

    /**
     * 특정 사용자가 하나 이상의 채팅방에 참여하고 있는지 여부를 확인합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}가 하나 이상의 {@link UserChatRoom} 기록을 가지고 있는지 여부를 확인하여
     * {@code true} 또는 {@code false}를 반환합니다.
     * </p>
     *
     * @param user 참여 기록의 존재 여부를 확인할 {@link User} 엔티티 객체
     * @return 주어진 사용자가 하나 이상의 채팅방에 참여하고 있을 경우 {@code true}, 그렇지 않을 경우 {@code false}
     */
    boolean existsByUser(User user);

    /**
     * 특정 사용자의 모든 채팅방 참여 기록을 삭제합니다.
     *
     * <p>
     * 이 메서드는 주어진 {@code user}에 속한 모든 {@link UserChatRoom} 기록을 데이터베이스에서 삭제합니다.
     * </p>
     *
     * @param user 삭제할 채팅방 참여 기록을 소유한 {@link User} 엔티티 객체
     */
    void deleteByUser(User user);
}
