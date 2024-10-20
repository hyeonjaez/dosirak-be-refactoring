package com.example.dosirakbe.domain.message.repository;

import com.example.dosirakbe.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(Long chatRoomId, LocalDateTime createdAt);
}
