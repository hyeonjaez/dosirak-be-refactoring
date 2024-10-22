package com.example.dosirakbe.domain.message.repository;

import com.example.dosirakbe.domain.message.entity.Message;
import com.example.dosirakbe.domain.message.entity.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(Long chatRoomId, LocalDateTime createdAt);

    Optional<Message> findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(Long chatRoomId, MessageType messageType);
}
