package com.example.dosirakbe.domain.chat_room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomByUserResponse {
    private Long id;
    private String title;
    private String image;
    private Long personCount;
    private String lastMessage;
}
