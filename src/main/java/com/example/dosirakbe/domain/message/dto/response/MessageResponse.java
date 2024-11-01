package com.example.dosirakbe.domain.message.dto.response;

import com.example.dosirakbe.domain.message.entity.MessageType;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private String content;
    private MessageType messageType;
    private LocalDateTime createdAt;
    private UserChatRoomResponse userChatRoomResponse;
    private Long chatRoomId;
}
