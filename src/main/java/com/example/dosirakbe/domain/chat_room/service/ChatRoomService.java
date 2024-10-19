package com.example.dosirakbe.domain.chat_room.service;

import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomInformationResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomResponse;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.dto.mapper.ChatRoomMapper;
import com.example.dosirakbe.domain.chat_room.repository.ChatRoomRepository;
import com.example.dosirakbe.domain.message.dto.mapper.MessageMapper;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.entity.Message;
import com.example.dosirakbe.domain.message.repository.MessageRepository;
import com.example.dosirakbe.domain.user.dto.mapper.UserMapper;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_chat_room.entity.UserChatRoom;
import com.example.dosirakbe.domain.user_chat_room.repository.UserChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    public ChatRoomResponse createChatRoom(ChatRoomRegisterRequest createRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(); //TODO 예외 처리
        ChatRoom chatRoom = new ChatRoom(createRequest.getTitle());

        userChatRoomRepository.save(new UserChatRoom(chatRoom, user));
        return chatRoomMapper.mapToChatRoomResponse(chatRoomRepository.save(chatRoom));
    }


    public void joinChatRoom(User user, ChatRoom chatRoom) {
        if (userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)) {
            //TODO 유저가 이미 해당 채팅방에 들어와 있는지 validation 확인
        }

        userChatRoomRepository.save(new UserChatRoom(chatRoom, user));
        chatRoom.upPersonCount();
        chatRoomRepository.save(chatRoom);
    }

    @Transactional(readOnly = true)
    public boolean isFirstTimeEntry(User user, ChatRoom chatRoom) {
        return !userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(); //TODO exception 처리
    }


    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllByUser(Long userId) {  //TODO 이거 join 으로 하나의 쿼리로 가능 할듯?
        User user = userRepository.findById(userId).orElseThrow(); //TODO 예외 처리
        List<UserChatRoom> allByUserId = userChatRoomRepository.findAllByUser(user);

        return allByUserId.stream()
                .map(a -> chatRoomMapper.mapToChatRoomResponse(a.getChatRoom()))
                .toList();
    }

    public void leaveChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId).orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();

        UserChatRoom userChatRoom =
                userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)
                        .orElseThrow(); //TODO 예외처리
        userChatRoomRepository.delete(userChatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllChatRooms() { // TODO 내가 참여하고 있는 방 filter 처리를 해줘야겠지?
        return chatRoomRepository.findAll().stream()
                .map(chatRoomMapper::mapToChatRoomResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ChatRoomInformationResponse findMessagesByChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId).orElseThrow();  // TODO 예외 처리 필요
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom).orElseThrow();

        List<Message> chatRoomMessageByUser = messageRepository.findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(chatRoom.getId(), userChatRoom.getCreatedAt());
        List<MessageResponse> messageList = messageMapper.mapToMessageResponseList(chatRoomMessageByUser);

        List<User> chatRoomUserList = userChatRoomRepository.findAllByChatRoom(chatRoom).stream().map(UserChatRoom::getUser).toList();
        List<UserChatRoomResponse> userList = userMapper.mapToUserChatRoomResponses(chatRoomUserList);

        return new ChatRoomInformationResponse(messageList, userList);
    }

    @Transactional(readOnly = true)
    public List<UserChatRoomResponse> findUserChatRooms(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(); //TODO exception
        List<User> userList = userChatRoomRepository.findAllByChatRoom(chatRoom).stream()
                .map(UserChatRoom::getUser)
                .toList();

        return userMapper.mapToUserChatRoomResponses(userList);

    }


}
