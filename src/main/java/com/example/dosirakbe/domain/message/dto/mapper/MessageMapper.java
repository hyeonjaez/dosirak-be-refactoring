package com.example.dosirakbe.domain.message.dto.mapper;

import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MessageMapper {

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "chatRoom.id", target = "chatRoomId")
    @Mapping(source = "beforeMessage", target = "beforeMessage")
    MessageResponse mapToMessageResponse(Message message);
}
