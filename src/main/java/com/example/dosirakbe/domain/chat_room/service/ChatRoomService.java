package com.example.dosirakbe.domain.chat_room.service;

import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomBriefResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomByUserResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomInformationResponse;
import com.example.dosirakbe.domain.chat_room.dto.response.ChatRoomResponse;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.dto.mapper.ChatRoomMapper;
import com.example.dosirakbe.domain.chat_room.repository.ChatRoomRepository;
import com.example.dosirakbe.domain.message.dto.mapper.MessageMapper;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.entity.Message;
import com.example.dosirakbe.domain.message.entity.MessageType;
import com.example.dosirakbe.domain.message.repository.MessageRepository;
import com.example.dosirakbe.domain.user.dto.mapper.UserMapper;
import com.example.dosirakbe.domain.user.dto.response.UserChatRoomResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.domain.user_chat_room.entity.UserChatRoom;
import com.example.dosirakbe.domain.user_chat_room.repository.UserChatRoomRepository;
import com.example.dosirakbe.domain.zone_category.entity.ZoneCategory;
import com.example.dosirakbe.domain.zone_category.repository.ZoneCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


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
    private final ZoneCategoryRepository zoneCategoryRepository;

    public ChatRoomResponse createChatRoom(ChatRoomRegisterRequest createRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(); //TODO 예외 처리
        ZoneCategory zoneCategory = zoneCategoryRepository.findByName(createRequest.getZoneCategoryName()).orElseThrow();

        ChatRoom chatRoom = new ChatRoom(createRequest.getTitle(), createRequest.getExplanation(), zoneCategory);

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
    public List<ChatRoomByUserResponse> findAllByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(); //TODO 예외 처리
        List<UserChatRoom> allByUserId = userChatRoomRepository.findAllByUser(user);

        return allByUserId.stream()
                .map(userChatRoom -> {
                    ChatRoom chatRoom = userChatRoom.getChatRoom();
                    Optional<Message> lastMessage =
                            messageRepository.findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(chatRoom.getId(), MessageType.CHAT);
                    ChatRoomByUserResponse chatRoomByUserResponse =
                            chatRoomMapper.mapToChatRoomByUserResponse(chatRoom);
                    lastMessage.ifPresent(message -> chatRoomByUserResponse.setLastMessage(message.getContent()));

                    return chatRoomByUserResponse;
                }).toList();
    }

    public void leaveChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId).orElseThrow();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();

        UserChatRoom userChatRoom =
                userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)
                        .orElseThrow(); //TODO 예외처리
        userChatRoomRepository.delete(userChatRoom);
        chatRoom.downPersonCount();
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
        List<User> chatRoomUserList = userChatRoomRepository.findAllByChatRoom(chatRoom).stream().map(UserChatRoom::getUser).toList();

        List<MessageResponse> messageList = messageMapper.mapToMessageResponseList(chatRoomMessageByUser);
        List<UserChatRoomResponse> userList = userMapper.mapToUserChatRoomResponses(chatRoomUserList);

        return new ChatRoomInformationResponse(chatRoom.getPersonCount(), chatRoom.getExplanation(), messageList, userList);
    }

    @Transactional(readOnly = true)
    public List<UserChatRoomResponse> findUserChatRooms(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(); //TODO exception
        List<User> userList = userChatRoomRepository.findAllByChatRoom(chatRoom).stream()
                .map(UserChatRoom::getUser)
                .toList();

        return userMapper.mapToUserChatRoomResponses(userList);

    }

    @Transactional(readOnly = true)
    public List<ChatRoomBriefResponse> findAllChatRoomBySearchAndSort(String sort, String search) {
        List<ChatRoom> chatRooms;
        Sort sorting;

        if ("popular".equalsIgnoreCase(sort)) {
            sorting = Sort.by(Sort.Direction.DESC, "personCount");
        } else {
            sorting = Sort.by(Sort.Direction.DESC, "createdAt");
        }

        if (Objects.nonNull(search) && !search.isEmpty()) {
            chatRooms = chatRoomRepository.findByTitleContainingIgnoreCase(search, sorting);
        } else {
            chatRooms = chatRoomRepository.findAll(sorting);
        }

        return chatRoomMapper.mapToChatRoomBriefResponseList(chatRooms);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomBriefResponse> findChatRoomByZoneCategory(Long userId, String zoneCategoryName) {
        User user = userRepository.findById(userId).orElseThrow();  // TODO 예외 처리 필요
        ZoneCategory zoneCategory = zoneCategoryRepository.findByName(zoneCategoryName).orElseThrow();

        List<ChatRoom> chatRoomsByZoneCategoryAndNotJoinedByUser = chatRoomRepository.findChatRoomsByZoneCategoryAndNotJoinedByUser(zoneCategory, user);

        return chatRoomMapper.mapToChatRoomBriefResponseList(chatRoomsByZoneCategoryAndNotJoinedByUser);
    }


}
