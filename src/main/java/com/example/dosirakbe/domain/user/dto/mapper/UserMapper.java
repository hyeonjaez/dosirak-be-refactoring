package com.example.dosirakbe.domain.user.dto.mapper;

import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import com.example.dosirakbe.domain.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    UserChatRoomResponse mapToUserChatRoomResponse(User user);

    List<UserChatRoomResponse> mapToUserChatRoomResponses(List<User> users);


}
