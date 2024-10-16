package com.example.dosirakbe.domain.user_chat_room.repository;

import com.example.dosirakbe.domain.user_chat_room.entity.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    List<UserChatRoom> findAllByUserId(Long userId);

    Optional<UserChatRoom> findByUserIdAndRoomId(Long userId, Long roomId);
}
