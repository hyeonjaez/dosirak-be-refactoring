package com.example.dosirakbe.domain.user_chat_room.repository;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user_chat_room.entity.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    List<UserChatRoom> findAllByUser(User user);

    Optional<UserChatRoom> findByUserAndChatRoom(User user, ChatRoom chatRoom);

    boolean existsByUserAndChatRoom(User user, ChatRoom chatRoom);

    List<UserChatRoom> findAllByChatRoom(ChatRoom chatRoom);
}
