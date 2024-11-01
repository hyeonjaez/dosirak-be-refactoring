package com.example.dosirakbe.domain.message.dto.mapper;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.entity.Message;
import com.example.dosirakbe.domain.message.entity.MessageType;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import com.example.dosirakbe.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-01T14:56:05+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class MessageMapperImpl implements MessageMapper {

    @Override
    public MessageResponse mapToMessageResponse(Message message) {
        if ( message == null ) {
            return null;
        }

        UserChatRoomResponse userChatRoomResponse = null;
        Long chatRoomId = null;
        Long id = null;
        String content = null;
        MessageType messageType = null;
        LocalDateTime createdAt = null;

        userChatRoomResponse = userToUserChatRoomResponse( message.getUser() );
        chatRoomId = messageChatRoomId( message );
        id = message.getId();
        content = message.getContent();
        messageType = message.getMessageType();
        createdAt = message.getCreatedAt();

        MessageResponse messageResponse = new MessageResponse( id, content, messageType, createdAt, userChatRoomResponse, chatRoomId );

        return messageResponse;
    }

    @Override
    public List<MessageResponse> mapToMessageResponseList(List<Message> messages) {
        if ( messages == null ) {
            return null;
        }

        List<MessageResponse> list = new ArrayList<MessageResponse>( messages.size() );
        for ( Message message : messages ) {
            list.add( mapToMessageResponse( message ) );
        }

        return list;
    }

    protected UserChatRoomResponse userToUserChatRoomResponse(User user) {
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

    private Long messageChatRoomId(Message message) {
        if ( message == null ) {
            return null;
        }
        ChatRoom chatRoom = message.getChatRoom();
        if ( chatRoom == null ) {
            return null;
        }
        Long id = chatRoom.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
