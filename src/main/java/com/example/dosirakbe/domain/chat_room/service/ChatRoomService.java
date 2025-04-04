package com.example.dosirakbe.domain.chat_room.service;

import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
import com.example.dosirakbe.domain.chat_room.dto.response.*;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.dto.mapper.ChatRoomMapper;
import com.example.dosirakbe.domain.chat_room.repository.ChatRoomRepository;
import com.example.dosirakbe.domain.green_commit.event.GreenCommitEvent;
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
import com.example.dosirakbe.global.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.service<br>
 * fileName       : ChatRoomService<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
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
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 새로운 채팅방을 생성합니다.
     *
     * <p>
     * 이 메서드는 사용자가 요청한 채팅방 정보를 기반으로 새로운 채팅방을 생성하며,
     * 선택적으로 이미지를 업로드할 수 있습니다. 채팅방 생성 후, 활동 로그를 기록하고,
     * 생성된 채팅방 정보를 반환합니다.
     * </p>
     *
     * @param file          업로드된 파일 (선택 사항)
     * @param createRequest 채팅방 생성 요청 데이터
     * @param userId        채팅방을 생성하는 사용자의 ID
     * @return 생성된 채팅방의 상세 정보를 {@link ChatRoomResponse} 형태로 반환합니다.
     * @throws ApiException 데이터 검증 실패 또는 관련 데이터가 존재하지 않을 경우 발생합니다.
     */
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

        eventPublisher.publishEvent(new GreenCommitEvent(this, user.getUserId(), chatRoom.getId(), ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION));

        return chatRoomMapper.mapToChatRoomResponse(chatRoomRepository.save(chatRoom));
    }

    /**
     * 사용자를 특정 채팅방에 참여시킵니다.
     *
     * <p>
     * 이 메서드는 사용자가 이미 채팅방에 참여하고 있는지 확인하고,
     * 참여하지 않은 경우 채팅방에 사용자를 추가합니다. 채팅방의 인원 수를 증가시킵니다.
     * </p>
     *
     * @param user     채팅방에 참여할 사용자
     * @param chatRoom 참여할 채팅방
     * @throws ApiException 사용자가 이미 채팅방에 참여하고 있을 경우 발생합니다.
     */
    public void joinChatRoom(User user, ChatRoom chatRoom) {
        if (userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)) {
            throw new ApiException(ExceptionEnum.CONFLICT);
        }

        userChatRoomRepository.save(new UserChatRoom(chatRoom, user));
        chatRoom.upPersonCount();
        chatRoomRepository.save(chatRoom);
    }

    /**
     * 사용자가 특정 채팅방에 처음 참여하는지 여부를 확인합니다.
     *
     * <p>
     * 이 메서드는 사용자가 해당 채팅방에 이미 참여하고 있는지를 확인하여,
     * 처음 참여하는 경우 {@code true}를 반환하고, 그렇지 않으면 {@code false}를 반환합니다.
     * </p>
     *
     * @param user     확인할 사용자
     * @param chatRoom 확인할 채팅방
     * @return 사용자가 처음 참여하는지 여부
     */
    @Transactional(readOnly = true)
    public boolean isFirstTimeEntry(User user, ChatRoom chatRoom) {
        return !userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom);
    }

    /**
     * 채팅방 ID로 채팅방을 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 ID에 해당하는 채팅방을 데이터베이스에서 조회하며,
     * 존재하지 않을 경우 {@link ApiException}을 발생시킵니다.
     * </p>
     *
     * @param chatRoomId 조회할 채팅방의 ID
     * @return 조회된 {@link ChatRoom} 엔티티
     * @throws ApiException 채팅방이 존재하지 않을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public ChatRoom findChatRoomById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
    }

    /**
     * 사용자가 참여하고 있는 모든 채팅방의 상세 정보를 조회합니다.
     *
     * <p>
     * 이 메서드는 사용자가 참여하고 있는 모든 채팅방을 조회하고,
     * 각 채팅방의 마지막 메시지 시간을 포함한 상세 정보를 {@link UserChatRoomParticipationResponse} 리스트로 반환합니다.
     * </p>
     *
     * @param userId 조회할 사용자의 ID
     * @return 사용자가 참여하고 있는 모든 채팅방의 상세 정보를 포함한 리스트
     * @throws ApiException 사용자가 존재하지 않을 경우 발생합니다.
     */
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

    /**
     * 사용자가 참여하고 있는 모든 채팅방의 간략한 정보를 조회합니다.
     *
     * <p>
     * 이 메서드는 사용자가 참여하고 있는 모든 채팅방을 조회하고,
     * 각 채팅방의 마지막 메시지 내용을 포함한 간략한 정보를 {@link UserChatRoomBriefParticipationResponse} 리스트로 반환합니다.
     * </p>
     *
     * @param userId 조회할 사용자의 ID
     * @return 사용자가 참여하고 있는 모든 채팅방의 간략한 정보를 포함한 리스트
     * @throws ApiException 사용자가 존재하지 않을 경우 발생합니다.
     */
    public List<UserChatRoomBriefParticipationResponse> findAllBriefByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));
        List<UserChatRoom> allByUserId = userChatRoomRepository.findAllByUser(user);

        return allByUserId.stream()
                .map(userChatRoom -> {
                    ChatRoom chatRoom = userChatRoom.getChatRoom();
                    Optional<Message> lastMessage =
                            messageRepository.findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(chatRoom.getId(), MessageType.CHAT);
                    UserChatRoomBriefParticipationResponse userChatRoomBriefParticipationResponse =
                            chatRoomMapper.mapToUserChatRoomBriefParticipationResponse(chatRoom);
                    lastMessage.ifPresent(message -> userChatRoomBriefParticipationResponse.setLastMessage(message.getContent()));

                    return userChatRoomBriefParticipationResponse;
                })
                .toList();
    }

    /**
     * 사용자가 특정 채팅방을 떠납니다.
     *
     * <p>
     * 이 메서드는 사용자가 특정 채팅방에서 떠나도록 처리하며,
     * 채팅방의 인원 수를 감소시킵니다.
     * </p>
     *
     * @param userId     사용자의 ID
     * @param chatRoomId 떠날 채팅방의 ID
     * @throws ApiException 사용자가 존재하지 않거나, 채팅방에 참여하고 있지 않을 경우 발생합니다.
     */
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

    /**
     * 특정 채팅방의 상세 정보를 조회합니다.
     *
     * <p>
     * 이 메서드는 사용자가 특정 채팅방에 참여하고 있는지 확인한 후,
     * 채팅방의 인원 수, 설명, 메시지 목록, 사용자 목록을 포함한 상세 정보를 {@link ChatRoomInformationResponse} 형태로 반환합니다.
     * </p>
     *
     * @param userId     조회할 사용자의 ID
     * @param chatRoomId 조회할 채팅방의 ID
     * @return 채팅방의 상세 정보를 포함한 {@link ChatRoomInformationResponse}
     * @throws ApiException 사용자가 존재하지 않거나, 채팅방에 참여하고 있지 않을 경우 발생합니다.
     */
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

    /**
     * 특정 지역 카테고리에 속한 모든 채팅방을 검색 및 정렬하여 조회합니다.
     *
     * <p>
     * 이 메서드는 주어진 지역 카테고리에 속한 채팅방을 검색 키워드와 정렬 방식에 따라 조회하고,
     * {@link ChatRoomBriefResponse} 리스트로 반환합니다.
     * </p>
     *
     * @param userId           조회할 사용자의 ID
     * @param zoneCategoryName 조회할 지역 카테고리의 이름
     * @param sort             정렬 방식 ("popular" 또는 "recent")
     * @param search           검색어 (선택 사항)
     * @return 검색 및 정렬된 채팅방의 간략한 정보를 포함한 리스트
     * @throws ApiException 지역 카테고리가 존재하지 않을 경우 발생합니다.
     */
    @Transactional(readOnly = true)
    public List<ChatRoomBriefResponse> findAllChatRoomBySearchAndSort(Long userId, String zoneCategoryName, String sort, String search) {
        Sort sorting = "popular".equalsIgnoreCase(sort)
                ? Sort.by(Sort.Direction.DESC, "personCount")
                : Sort.by(Sort.Direction.DESC, "createdAt");

        ZoneCategory zoneCategory = zoneCategoryRepository.findByName(zoneCategoryName)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

        String searchQuery = (Objects.nonNull(search)) ? search.trim() : null;

        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByZoneCategoryAndNotJoinedByUser(
                zoneCategory, user, searchQuery, sorting);

        return chatRoomMapper.mapToChatRoomBriefResponseList(chatRooms);
    }

    /**
     * 파일과 채팅방 생성 요청을 검증합니다.
     *
     * <p>
     * 이 메서드는 채팅방 생성 시 필수적인 이미지가 제공되었는지 확인하고,
     * 기본 이미지와 파일이 동시에 제공되거나, 둘 다 제공되지 않은 경우 예외를 발생시킵니다.
     * </p>
     *
     * @param file          업로드된 파일 (선택 사항)
     * @param createRequest 채팅방 생성 요청 데이터
     * @throws ApiException 유효하지 않은 요청일 경우 발생합니다.
     */
    private void validationFile(MultipartFile file, ChatRoomRegisterRequest createRequest) {
        if (Objects.isNull(createRequest.getDefaultImage()) && (Objects.isNull(file) || file.isEmpty())) {
            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
        }
        if (Objects.nonNull(createRequest.getDefaultImage()) && Objects.nonNull(file)) {
            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
        }
    }
}