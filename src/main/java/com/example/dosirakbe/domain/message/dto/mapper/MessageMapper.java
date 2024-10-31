package com.example.dosirakbe.domain.message.dto.mapper;

import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MessageMapper {

    @Mapping(source = "user.userId", target = "userChatRoomResponse.userId")
    @Mapping(source = "user.nickName", target = "userChatRoomResponse.nickName")
    @Mapping(source = "user.profileImg", target = "userChatRoomResponse.profileImg")
    @Mapping(source = "chatRoom.id", target = "chatRoomId")
    MessageResponse mapToMessageResponse(Message message);


    List<MessageResponse> mapToMessageResponseList(List<Message> messages);
}
