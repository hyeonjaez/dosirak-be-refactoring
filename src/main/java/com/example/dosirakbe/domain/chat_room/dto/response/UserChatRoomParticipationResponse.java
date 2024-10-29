package com.example.dosirakbe.domain.chat_room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UserChatRoomParticipationResponse {
    private Long id;
    private String title;
    private String image;
    private String explanation;
    private LocalDateTime lastMessageTime;
}
