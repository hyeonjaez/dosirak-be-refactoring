package com.example.dosirakbe.domain.chat_room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomBriefResponse {
    private Long id;
    private String title;
    private Long personCount;
    private String explanation;
}
