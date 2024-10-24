package com.example.dosirakbe.domain.chat_room.dto.mapper;

import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomResponse;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-10-23T18:52:12+0900",
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
}
