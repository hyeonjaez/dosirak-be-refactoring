package com.example.dosirakbe.domain.message.service;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.repository.ChatRoomRepository;
import com.example.dosirakbe.domain.message.dto.mapper.MessageMapper;
import com.example.dosirakbe.domain.message.dto.request.MessageRegisterRequest;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.entity.Message;
import com.example.dosirakbe.domain.message.entity.MessageType;
import com.example.dosirakbe.domain.message.repository.MessageRepository;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_chat_room.repository.UserChatRoomRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageMapper messageMapper;
    private final UserChatRoomRepository userChatRoomRepository;
    private static final String FIRST_JOIN_MESSAGE = "님이 들어왔습니다.";

    public MessageResponse createMessage(Long chatRoomId, MessageRegisterRequest messageRegisterRequest) {
        User user = userRepository.findById(messageRegisterRequest.getUserId())
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        if (!userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
        }

        Message message = new Message(messageRegisterRequest.getContent(), messageRegisterRequest.getMessageType(), user, chatRoom);
        Message saveMessage = messageRepository.save(message);

        return messageMapper.mapToMessageResponse(saveMessage);
    }

    public MessageResponse firstJoinMessage(User user, ChatRoom chatRoom) {
        Message message = new Message(user.getNickName() + FIRST_JOIN_MESSAGE, MessageType.JOIN, user, chatRoom);
        Message saveMessage = messageRepository.save(message);

        return messageMapper.mapToMessageResponse(saveMessage);
    }
}
