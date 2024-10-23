package com.example.dosirakbe.domain.chat_room.dto.mapper;

import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomBriefResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomByUserResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomResponse;
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
}
