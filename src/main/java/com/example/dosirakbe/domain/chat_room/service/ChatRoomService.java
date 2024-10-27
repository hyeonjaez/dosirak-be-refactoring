package com.example.dosirakbe.domain.chat_room.service;

import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
import com.example.dosirakbe.domain.chat_room.dto.response.*;
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
import com.example.dosirakbe.global.config.S3Uploader;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final S3Uploader s3Uploader;

    public ChatRoomResponse createChatRoom(MultipartFile file, ChatRoomRegisterRequest createRequest, Long userId) {
        validationFile(file, createRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        ZoneCategory zoneCategory = zoneCategoryRepository.findByName(createRequest.getZoneCategoryName())
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        String imageUrl = createRequest.getDefaultImage();

        if (Objects.nonNull(file)) {
            imageUrl = s3Uploader.saveFile(file);
        }

        ChatRoom chatRoom = new ChatRoom(createRequest.getTitle(), createRequest.getExplanation(), zoneCategory, imageUrl);

        userChatRoomRepository.save(new UserChatRoom(chatRoom, user));
        return chatRoomMapper.mapToChatRoomResponse(chatRoomRepository.save(chatRoom));
    }


    public void joinChatRoom(User user, ChatRoom chatRoom) {
        if (userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new ApiException(ExceptionEnum.CONFLICT);
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
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<UserChatRoomParticipationResponse> findAllByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        List<UserChatRoom> allByUserId = userChatRoomRepository.findAllByUser(user);

        return allByUserId.stream()
                .map(userChatRoom -> {
                    ChatRoom chatRoom = userChatRoom.getChatRoom();
                    Optional<Message> lastMessage =
                            messageRepository.findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(chatRoom.getId(), MessageType.CHAT);
                    UserChatRoomParticipationResponse userChatRoomParticipationResponse =
                            chatRoomMapper.mapToUserChatRoomParticipationResponse(chatRoom);
                    lastMessage.ifPresent(message -> userChatRoomParticipationResponse.setLastMessageTime(message.getCreatedAt()));

                    return userChatRoomParticipationResponse;
                }).toList();
    }

    public List<UserChatRoomBriefParticipationResponse> findAllBriefByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        List<UserChatRoom> allByUserId = userChatRoomRepository.findAllByUser(user);

        return allByUserId.stream().map(userChatRoom -> {
            ChatRoom chatRoom = userChatRoom.getChatRoom();
            Optional<Message> lastMessage =
                    messageRepository.findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(chatRoom.getId(), MessageType.CHAT);
            UserChatRoomBriefParticipationResponse userChatRoomBriefParticipationResponse =
                    chatRoomMapper.mapToUserChatRoomBriefParticipationResponse(chatRoom);
            lastMessage.ifPresent(message -> userChatRoomBriefParticipationResponse.setLastMessage(message.getContent()));

            return userChatRoomBriefParticipationResponse;
        }).toList();

    }

    public void leaveChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        userChatRoomRepository.delete(userChatRoom);
        chatRoom.downPersonCount();
    }

    @Transactional(readOnly = true)
    public ChatRoomInformationResponse findMessagesByChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        UserChatRoom userChatRoom = userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        List<Message> chatRoomMessageByUser = messageRepository.findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(chatRoom.getId(), userChatRoom.getCreatedAt());
        List<User> chatRoomUserList = userChatRoomRepository.findAllByChatRoom(chatRoom).stream().map(UserChatRoom::getUser).toList();

        List<MessageResponse> messageList = messageMapper.mapToMessageResponseList(chatRoomMessageByUser);
        List<UserChatRoomResponse> userList = userMapper.mapToUserChatRoomResponses(chatRoomUserList);

        return new ChatRoomInformationResponse(chatRoom.getPersonCount(), chatRoom.getExplanation(), messageList, userList);
    }

    @Transactional(readOnly = true)
    public List<UserChatRoomResponse> findUserChatRooms(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        List<User> userList = userChatRoomRepository.findAllByChatRoom(chatRoom).stream()
                .map(UserChatRoom::getUser)
                .toList();

        return userMapper.mapToUserChatRoomResponses(userList);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomBriefResponse> findAllChatRoomBySearchAndSort(Long userId, String zoneCategoryName, String sort, String search) {
        Sort sorting = "popular".equalsIgnoreCase(sort)
                ? Sort.by(Sort.Direction.DESC, "personCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        ZoneCategory zoneCategory = zoneCategoryRepository.findByName(zoneCategoryName)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByZoneCategoryAndNotJoinedByUser(
                zoneCategory, user, search.trim(), sorting);

        return chatRoomMapper.mapToChatRoomBriefResponseList(chatRooms);
    }

    private void validationFile(MultipartFile file, ChatRoomRegisterRequest createRequest) {
        if (Objects.isNull(createRequest.getDefaultImage()) && (Objects.isNull(file) || file.isEmpty())) {
            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
        }
        if (Objects.nonNull(createRequest.getDefaultImage()) && Objects.nonNull(file)) {
            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
        }
    }
}