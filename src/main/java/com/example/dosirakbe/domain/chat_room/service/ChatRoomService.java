package com.example.dosirakbe.domain.chat_room.service;

import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomResponse;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.dto.mapper.ChatRoomMapper;
import com.example.dosirakbe.domain.chat_room.repository.ChatRoomRepository;
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


    public ChatRoomResponse createChatRoom(ChatRoomRegisterRequest createRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(); //TODO 예외 처리
        ChatRoom chatRoom = new ChatRoom(createRequest.getTitle());

        userChatRoomRepository.save(new UserChatRoom(chatRoom, user));
        return chatRoomMapper.mapToChatRoomResponse(chatRoomRepository.save(chatRoom));
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllByUser(Long userId) {  //TODO 이거 join 으로 하나의 쿼리로 가능 할듯?
        User user = userRepository.findById(userId).orElseThrow(); //TODO 예외 처리
        List<UserChatRoom> allByUserId = userChatRoomRepository.findAllByUserId(user.getUserId());

        return allByUserId.stream()
                .map(a -> chatRoomMapper.mapToChatRoomResponse(a.getChatRoom()))
                .toList();
    }

    public ChatRoomResponse joinChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId).orElseThrow(); //TODO 예외처리
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(); //TODO 예외처리
        userChatRoomRepository.save(new UserChatRoom(chatRoom, user));
        chatRoom.upPersonCount();

        return chatRoomMapper.mapToChatRoomResponse(chatRoomRepository.save(chatRoom));
    }

    public void leaveChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId).orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();

        UserChatRoom userChatRoom =
                userChatRoomRepository.findByUserIdAndRoomId(user.getUserId(), chatRoom.getId())
                        .orElseThrow(); //TODO 예외처리
        userChatRoomRepository.delete(userChatRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findAllChatRooms() { // TODO 내가 참여하고 있는 방 filter 처리를 해줘야겠지?
        return chatRoomRepository.findAll().stream()
                .map(chatRoomMapper::mapToChatRoomResponse)
                .toList();
    }
}
