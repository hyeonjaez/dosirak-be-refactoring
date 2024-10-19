package com.example.dosirakbe.domain.chat_room.dto.response;

import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ChatRoomInformationResponse {
    private List<MessageResponse> messageList;
    private List<UserChatRoomResponse> userList;
}
