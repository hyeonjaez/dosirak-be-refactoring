package com.example.dosirakbe.domain.message.service;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.repository.ChatRoomRepository;
import com.example.dosirakbe.domain.message.dto.mapper.MessageMapper;
import com.example.dosirakbe.domain.message.dto.request.MessageRegisterRequest;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.entity.Message;
import com.example.dosirakbe.domain.message.entity.MessageType;
import com.example.dosirakbe.domain.message.repository.MessageRepository;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_chat_room.repository.UserChatRoomRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class MessageServiceTest {
    @InjectMocks
    private MessageService messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private UserChatRoomRepository userChatRoomRepository;


    @DisplayName("createMessage - 성공")
    @Test
    void createMessageTest() {
        Long userId = 1L;
        Long chatRoomId = 1L;
        String content = "Happy";
        MessageType messageType = MessageType.CHAT;

        MessageRegisterRequest request = new MessageRegisterRequest(content, messageType);

        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);

        Message savedMessage = new Message(100L, content, messageType, LocalDateTime.now(), user, chatRoom); // Assume ID is 100L

        UserChatRoomResponse userChatRoomResponse = mock(UserChatRoomResponse.class);
        MessageResponse expectedResponse = new MessageResponse(100L, content, messageType, LocalDateTime.now(), userChatRoomResponse, chatRoomId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(true);
        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);
        when(messageMapper.mapToMessageResponse(savedMessage)).thenReturn(expectedResponse);

        MessageResponse actualResponse = messageService.createMessage(userId, chatRoomId, request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, times(1)).existsByUserAndChatRoom(user, chatRoom);
        verify(messageRepository, times(1)).save(any(Message.class));
        verify(messageMapper, times(1)).mapToMessageResponse(savedMessage);
    }

    @DisplayName("createMessage - 사용자 미존재 시 예외 발생")
    @Test
    void createMessage_UserNotFound() {
        Long userId = 1L;
        Long chatRoomId = 1L;
        String content = "Happy";
        MessageType messageType = MessageType.CHAT;

        MessageRegisterRequest request = new MessageRegisterRequest(content, messageType);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            messageService.createMessage(userId, chatRoomId, request);
        });

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, never()).findById(anyLong());
        verify(userChatRoomRepository, never()).existsByUserAndChatRoom(any(User.class), any(ChatRoom.class));
        verify(messageRepository, never()).save(any(Message.class));
        verify(messageMapper, never()).mapToMessageResponse(any(Message.class));
    }

    @DisplayName("createMessage - 채팅방 미존재 시 예외 발생")
    @Test
    void createMessage_ChatRoomNotFound() {
        Long userId = 1L;
        Long chatRoomId = 1L;
        String content = "Happy";
        MessageType messageType = MessageType.CHAT;

        MessageRegisterRequest request = new MessageRegisterRequest(content, messageType);

        User user = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> {
            messageService.createMessage(userId, chatRoomId, request);
        });

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, never()).existsByUserAndChatRoom(any(User.class), any(ChatRoom.class));
        verify(messageRepository, never()).save(any(Message.class));
        verify(messageMapper, never()).mapToMessageResponse(any(Message.class));
    }

    @DisplayName("createMessage - 사용자가 채팅방에 속해있지 않을 시 예외 발생")
    @Test
    void createMessage_UserNotInChatRoom() {
        Long userId = 1L;
        Long chatRoomId = 1L;
        String content = "Happy";
        MessageType messageType = MessageType.CHAT;

        MessageRegisterRequest request = new MessageRegisterRequest(content, messageType);

        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> {
            messageService.createMessage(userId, chatRoomId, request);
        });

        assertEquals(ExceptionEnum.RUNTIME_EXCEPTION, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, times(1)).existsByUserAndChatRoom(user, chatRoom);
        verify(messageRepository, never()).save(any(Message.class));
        verify(messageMapper, never()).mapToMessageResponse(any(Message.class));
    }

    @DisplayName("firstJoinMessage - 성공")
    @Test
    void firstJoinMessage_Success() {
        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);

        String expectedContent = user.getNickName() + "님이 들어왔습니다.";
        MessageType messageType = MessageType.JOIN;

        Message savedMessage = mock(Message.class);
        UserChatRoomResponse userChatRoomResponse = mock(UserChatRoomResponse.class);
        MessageResponse expectedResponse = new MessageResponse(100L, expectedContent, messageType, LocalDateTime.now(), userChatRoomResponse, 1L);

        when(messageRepository.save(any(Message.class))).thenReturn(savedMessage);
        when(messageMapper.mapToMessageResponse(savedMessage)).thenReturn(expectedResponse);

        MessageResponse actualResponse = messageService.firstJoinMessage(user, chatRoom);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);

        verify(messageRepository, times(1)).save(any(Message.class));
        verify(messageMapper, times(1)).mapToMessageResponse(savedMessage);
    }

}