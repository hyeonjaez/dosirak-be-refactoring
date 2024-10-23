package com.example.dosirakbe.domain.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserChatRoomResponse {
    private Long userId;
    private String nickName;
    private String profileImg;
}
