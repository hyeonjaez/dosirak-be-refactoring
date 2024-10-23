package com.example.dosirakbe.domain.chat_room.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomSortFilterRequest {
    private String search;
    private String sort;
}
