package com.example.dosirakbe.domain.chat_room.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class UserChatRoomBriefParticipationResponse {
    private Long id;
    private String image;
    private String lastMessage;
}
