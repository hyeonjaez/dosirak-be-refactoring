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
import com.example.dosirakbe.domain.user_chat_room.entity.UserChatRoom;
import com.example.dosirakbe.domain.user_chat_room.repository.UserChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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

    public MessageResponse createMessage(MessageRegisterRequest messageRegisterRequest) {
        User user = userRepository.findById(messageRegisterRequest.getUserId()).orElseThrow();  // 예외 처리 필요
        ChatRoom chatRoom = chatRoomRepository.findById(messageRegisterRequest.getChatRoomId()).orElseThrow();

        if (!userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)) {
            //TODO throw
        }
        Message beforeMessage = null;
        if (Objects.nonNull(messageRegisterRequest.getBeforeMessageId())) {
            beforeMessage = messageRepository.findById(messageRegisterRequest.getBeforeMessageId()).orElseThrow();
        }

        Message message = new Message(messageRegisterRequest.getContent(), messageRegisterRequest.getMessageType(), user, chatRoom, beforeMessage);
        Message saveMessage = messageRepository.save(message);

        return messageMapper.mapToMessageResponse(saveMessage);
    }

    @Transactional(readOnly = true)
    public List<Message> findMessagesByChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId).orElseThrow();  // TODO 예외 처리 필요
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom).orElseThrow();

        return messageRepository.findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(chatRoom.getId(), userChatRoom.getCreatedAt());
    }

    public MessageResponse firstJoinMessage(User user, ChatRoom chatRoom) {
        Message message = new Message(user.getNickName() + FIRST_JOIN_MESSAGE, MessageType.JOIN, user, chatRoom, null);
        Message saveMessage = messageRepository.save(message);

        return messageMapper.mapToMessageResponse(saveMessage);
    }
}
