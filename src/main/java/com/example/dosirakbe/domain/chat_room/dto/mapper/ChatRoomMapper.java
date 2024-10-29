package com.example.dosirakbe.domain.chat_room.dto.mapper;

import com.example.dosirakbe.domain.chat_room.dto.response.*;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ChatRoomMapper {
    ChatRoomResponse mapToChatRoomResponse(ChatRoom chatRoom);

    @Mapping(target = "lastMessage", ignore = true)
    ChatRoomByUserResponse mapToChatRoomByUserResponse(ChatRoom chatRoom);

    ChatRoomBriefResponse mapToChatRoomBriefResponse(ChatRoom chatRoom);

    List<ChatRoomBriefResponse> mapToChatRoomBriefResponseList(List<ChatRoom> chatRooms);

    @Mapping(target = "lastMessageTime", ignore = true)
    UserChatRoomParticipationResponse mapToUserChatRoomParticipationResponse(ChatRoom chatRoom);

    @Mapping(target = "lastMessage", ignore = true)
    UserChatRoomBriefParticipationResponse mapToUserChatRoomBriefParticipationResponse(ChatRoom chatRoom);
}
