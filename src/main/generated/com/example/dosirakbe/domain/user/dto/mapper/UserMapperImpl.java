package com.example.dosirakbe.domain.user.dto.mapper;

import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import com.example.dosirakbe.domain.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-31T15:43:23+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserChatRoomResponse mapToUserChatRoomResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long userId = null;
        String nickName = null;
        String profileImg = null;

        userId = user.getUserId();
        nickName = user.getNickName();
        profileImg = user.getProfileImg();

        UserChatRoomResponse userChatRoomResponse = new UserChatRoomResponse( userId, nickName, profileImg );

        return userChatRoomResponse;
    }

    @Override
    public List<UserChatRoomResponse> mapToUserChatRoomResponses(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserChatRoomResponse> list = new ArrayList<UserChatRoomResponse>( users.size() );
        for ( User user : users ) {
            list.add( mapToUserChatRoomResponse( user ) );
        }

        return list;
    }
}
