package com.example.dosirakbe.domain.chat_room.dto.mapper;

import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomBriefResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomByUserResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.UserChatRoomBriefParticipationResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.UserChatRoomParticipationResponse;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-29T23:50:14+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ChatRoomMapperImpl implements ChatRoomMapper {

    @Override
    public ChatRoomResponse mapToChatRoomResponse(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        Long personCount = null;

        id = chatRoom.getId();
        title = chatRoom.getTitle();
        personCount = chatRoom.getPersonCount();

        ChatRoomResponse chatRoomResponse = new ChatRoomResponse( id, title, personCount );

        return chatRoomResponse;
    }

    @Override
    public ChatRoomByUserResponse mapToChatRoomByUserResponse(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String image = null;
        Long personCount = null;

        id = chatRoom.getId();
        title = chatRoom.getTitle();
        image = chatRoom.getImage();
        personCount = chatRoom.getPersonCount();

        String lastMessage = null;

        ChatRoomByUserResponse chatRoomByUserResponse = new ChatRoomByUserResponse( id, title, image, personCount, lastMessage );

        return chatRoomByUserResponse;
    }

    @Override
    public ChatRoomBriefResponse mapToChatRoomBriefResponse(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String image = null;
        Long personCount = null;
        String explanation = null;

        id = chatRoom.getId();
        title = chatRoom.getTitle();
        image = chatRoom.getImage();
        personCount = chatRoom.getPersonCount();
        explanation = chatRoom.getExplanation();

        ChatRoomBriefResponse chatRoomBriefResponse = new ChatRoomBriefResponse( id, title, image, personCount, explanation );

        return chatRoomBriefResponse;
    }

    @Override
    public List<ChatRoomBriefResponse> mapToChatRoomBriefResponseList(List<ChatRoom> chatRooms) {
        if ( chatRooms == null ) {
            return null;
        }

        List<ChatRoomBriefResponse> list = new ArrayList<ChatRoomBriefResponse>( chatRooms.size() );
        for ( ChatRoom chatRoom : chatRooms ) {
            list.add( mapToChatRoomBriefResponse( chatRoom ) );
        }

        return list;
    }

    @Override
    public UserChatRoomParticipationResponse mapToUserChatRoomParticipationResponse(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String image = null;
        String explanation = null;

        id = chatRoom.getId();
        title = chatRoom.getTitle();
        image = chatRoom.getImage();
        explanation = chatRoom.getExplanation();

        LocalDateTime lastMessageTime = null;

        UserChatRoomParticipationResponse userChatRoomParticipationResponse = new UserChatRoomParticipationResponse( id, title, image, explanation, lastMessageTime );

        return userChatRoomParticipationResponse;
    }

    @Override
    public UserChatRoomBriefParticipationResponse mapToUserChatRoomBriefParticipationResponse(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        Long id = null;
        String image = null;

        id = chatRoom.getId();
        image = chatRoom.getImage();

        String lastMessage = null;

        UserChatRoomBriefParticipationResponse userChatRoomBriefParticipationResponse = new UserChatRoomBriefParticipationResponse( id, image, lastMessage );

        return userChatRoomBriefParticipationResponse;
    }
}
