package com.example.dosirakbe.domain.chat_room.service;

import com.example.dosirakbe.domain.chat_room.dto.mapper.ChatRoomMapper;
import com.example.dosirakbe.domain.chat_room.dto.request.ChatRoomRegisterRequest;
import com.example.dosirakbe.domain.chat_room.dto.response.*;
import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
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
import com.example.dosirakbe.global.config.S3Uploader;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserChatRoomRepository userChatRoomRepository;

    @Mock
    private ChatRoomMapper chatRoomMapper;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ZoneCategoryRepository zoneCategoryRepository;

    @Mock
    private S3Uploader s3Uploader;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    @DisplayName("createChatRoom - 성공 (파일 포함)")
    void createChatRoom_Success_WithFile() {
        Long userId = 1L;
        User user = mock(User.class);

        ChatRoomRegisterRequest createRequest = new ChatRoomRegisterRequest();
        ReflectionTestUtils.setField(createRequest, "title", "제목");
        ReflectionTestUtils.setField(createRequest, "explanation", "설명");
        ReflectionTestUtils.setField(createRequest, "zoneCategoryName", "지역카테고리");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        ZoneCategory zoneCategory = mock(ZoneCategory.class);
        when(zoneCategoryRepository.findByName(any(String.class))).thenReturn(Optional.of(zoneCategory));

        MultipartFile file = mock(MultipartFile.class);

        String uploadedImageUrl = "http://s3.amazonaws.com/bucket/image.png";
        when(s3Uploader.saveFile(any())).thenReturn(uploadedImageUrl);


        ChatRoom chatRoom = new ChatRoom("채팅방 제목", "채팅방 설명", zoneCategory, uploadedImageUrl);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
        when(userChatRoomRepository.save(any(UserChatRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatRoomResponse expected = new ChatRoomResponse(1L, "제목", 1L);
        when(chatRoomMapper.mapToChatRoomResponse(any(ChatRoom.class))).thenReturn(expected);

        ChatRoomResponse result = chatRoomService.createChatRoom(file, createRequest, userId);


        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getPersonCount(), result.getPersonCount());

        verify(s3Uploader, times(1)).saveFile(any(MultipartFile.class));
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(zoneCategoryRepository, times(1)).findByName(any(String.class));
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
        verify(userChatRoomRepository, times(1)).save(any(UserChatRoom.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(GreenCommitEvent.class));
        verify(chatRoomMapper, times(1)).mapToChatRoomResponse(any(ChatRoom.class));
    }

    @Test
    @DisplayName("createChatRoom - 성공 (파일 미 포함)")
    void createChatRoom_Success_notWithFile() {
        Long userId = 1L;
        User user = mock(User.class);

        ChatRoomRegisterRequest createRequest = new ChatRoomRegisterRequest();
        ReflectionTestUtils.setField(createRequest, "title", "제목");
        ReflectionTestUtils.setField(createRequest, "explanation", "설명");
        ReflectionTestUtils.setField(createRequest, "zoneCategoryName", "지역카테고리");
        ReflectionTestUtils.setField(createRequest, "defaultImage", "기본 이미지 입니다.");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        ZoneCategory zoneCategory = mock(ZoneCategory.class);
        when(zoneCategoryRepository.findByName(any(String.class))).thenReturn(Optional.of(zoneCategory));

        String defaultImage = "기본 이미지 입니다.";

        ChatRoom chatRoom = new ChatRoom("채팅방 제목", "채팅방 설명", zoneCategory, defaultImage);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(chatRoom);
        when(userChatRoomRepository.save(any(UserChatRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatRoomResponse expected = new ChatRoomResponse(1L, "제목", 1L);
        when(chatRoomMapper.mapToChatRoomResponse(any(ChatRoom.class))).thenReturn(expected);

        ChatRoomResponse result = chatRoomService.createChatRoom(null, createRequest, userId);


        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTitle(), result.getTitle());
        assertEquals(expected.getPersonCount(), result.getPersonCount());

        verify(s3Uploader, times(0)).saveFile(any(MultipartFile.class));
        verify(userRepository, times(1)).findById(any(Long.class));
        verify(zoneCategoryRepository, times(1)).findByName(any(String.class));
        verify(chatRoomRepository, times(1)).save(any(ChatRoom.class));
        verify(userChatRoomRepository, times(1)).save(any(UserChatRoom.class));
        verify(applicationEventPublisher, times(1)).publishEvent(any(GreenCommitEvent.class));
        verify(chatRoomMapper, times(1)).mapToChatRoomResponse(any(ChatRoom.class));
    }

    @Test
    @DisplayName("createChatRoom - 실패 (default image 미 포함 및 파일 미 포함)")
    void createChatRoomTest_Fail_notExistDefaultImageAndFile() {
        Long userId = 1L;

        ChatRoomRegisterRequest createRequest = new ChatRoomRegisterRequest();
        ReflectionTestUtils.setField(createRequest, "title", "제목");
        ReflectionTestUtils.setField(createRequest, "explanation", "설명");
        ReflectionTestUtils.setField(createRequest, "zoneCategoryName", "지역카테고리");


        ApiException apiException = assertThrows(ApiException.class, () -> chatRoomService.createChatRoom(null, createRequest, userId));

        assertEquals(ExceptionEnum.INVALID_REQUEST, apiException.getError());

        verify(s3Uploader, times(0)).saveFile(any(MultipartFile.class));
        verify(userRepository, times(0)).findById(any(Long.class));
        verify(zoneCategoryRepository, times(0)).findByName(any(String.class));
        verify(chatRoomRepository, times(0)).save(any(ChatRoom.class));
        verify(userChatRoomRepository, times(0)).save(any(UserChatRoom.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(GreenCommitEvent.class));
        verify(chatRoomMapper, times(0)).mapToChatRoomResponse(any(ChatRoom.class));
    }

    @Test
    @DisplayName("createChatRoom - 실패 (default image 미 포함 및 파일 비어 있음)")
    void createChatRoomTest_Fail_notExistDefaultImageAndEmptyFile() {
        MultipartFile file = new MockMultipartFile("file", new byte[0]);
        Long userId = 1L;

        ChatRoomRegisterRequest createRequest = new ChatRoomRegisterRequest();
        ReflectionTestUtils.setField(createRequest, "title", "제목");
        ReflectionTestUtils.setField(createRequest, "explanation", "설명");
        ReflectionTestUtils.setField(createRequest, "zoneCategoryName", "지역카테고리");


        ApiException apiException = assertThrows(ApiException.class, () -> chatRoomService.createChatRoom(file, createRequest, userId));

        assertEquals(ExceptionEnum.INVALID_REQUEST, apiException.getError());

        verify(s3Uploader, times(0)).saveFile(any(MultipartFile.class));
        verify(userRepository, times(0)).findById(any(Long.class));
        verify(zoneCategoryRepository, times(0)).findByName(any(String.class));
        verify(chatRoomRepository, times(0)).save(any(ChatRoom.class));
        verify(userChatRoomRepository, times(0)).save(any(UserChatRoom.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(GreenCommitEvent.class));
        verify(chatRoomMapper, times(0)).mapToChatRoomResponse(any(ChatRoom.class));

    }

    @DisplayName("createChatRoom - 실패 (default image 포함 및 파일 포함)")
    @Test
    void createChatRoomTest_Fail_existDefaultImageAndFile() {
        MultipartFile file = new MockMultipartFile("file", new byte[1234]);
        Long userId = 1L;

        ChatRoomRegisterRequest createRequest = new ChatRoomRegisterRequest();
        ReflectionTestUtils.setField(createRequest, "title", "제목");
        ReflectionTestUtils.setField(createRequest, "explanation", "설명");
        ReflectionTestUtils.setField(createRequest, "zoneCategoryName", "지역카테고리");
        ReflectionTestUtils.setField(createRequest, "defaultImage", "기본 이미지 입니다.");

        ApiException apiException = assertThrows(ApiException.class, () -> chatRoomService.createChatRoom(file, createRequest, userId));

        assertEquals(ExceptionEnum.INVALID_REQUEST, apiException.getError());

        verify(s3Uploader, times(0)).saveFile(any(MultipartFile.class));
        verify(userRepository, times(0)).findById(any(Long.class));
        verify(zoneCategoryRepository, times(0)).findByName(any(String.class));
        verify(chatRoomRepository, times(0)).save(any(ChatRoom.class));
        verify(userChatRoomRepository, times(0)).save(any(UserChatRoom.class));
        verify(applicationEventPublisher, times(0)).publishEvent(any(GreenCommitEvent.class));
        verify(chatRoomMapper, times(0)).mapToChatRoomResponse(any(ChatRoom.class));
    }

    @DisplayName("createChatRoom - 실패 (user 를 찾을 수 없음)")
    @Test
    void createChatRoomTest_Fail_userNotFound() {
        MultipartFile file = new MockMultipartFile("file", new byte[1234]);
        Long userId = 1L;

        ChatRoomRegisterRequest createRequest = new ChatRoomRegisterRequest();
        ReflectionTestUtils.setField(createRequest, "title", "제목");
        ReflectionTestUtils.setField(createRequest, "explanation", "설명");
        ReflectionTestUtils.setField(createRequest, "zoneCategoryName", "지역카테고리");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ApiException apiException = assertThrows(ApiException.class, () -> chatRoomService.createChatRoom(file, createRequest, userId));

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, apiException.getError());
    }

    @DisplayName("createChatRoom - 실패 (zoneCategory 찾을 수 없음)")
    @Test
    void createChatRoomTest_Fail_zoneCategoryNotFound() {
        MultipartFile file = new MockMultipartFile("file", new byte[1234]);
        Long userId = 1L;
        User user = mock(User.class);

        ChatRoomRegisterRequest createRequest = new ChatRoomRegisterRequest();
        ReflectionTestUtils.setField(createRequest, "title", "제목");
        ReflectionTestUtils.setField(createRequest, "explanation", "설명");
        ReflectionTestUtils.setField(createRequest, "zoneCategoryName", "지역카테고리");

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(zoneCategoryRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        ApiException apiException = assertThrows(ApiException.class, () -> chatRoomService.createChatRoom(file, createRequest, userId));

        assertEquals(ExceptionEnum.DATA_NOT_FOUND, apiException.getError());
    }

    @Test
    @DisplayName("joinChatRoom - 성공")
    void joinChatRoom_Success() {
        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        when(userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(false);

        chatRoomService.joinChatRoom(user, chatRoom);

        verify(userChatRoomRepository, times(1)).save(any(UserChatRoom.class));
        verify(chatRoom, times(1)).upPersonCount();
        verify(chatRoomRepository, times(1)).save(chatRoom);
    }

    @Test
    @DisplayName("joinChatRoom - 실패: 이미 채팅방에 참가한 경우")
    void joinChatRoom_Fail_AlreadyJoined() {
        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        when(userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(true);

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.joinChatRoom(user, chatRoom));
        assertEquals(ExceptionEnum.CONFLICT, exception.getError());

        verify(userChatRoomRepository, times(0)).save(any(UserChatRoom.class));
        verify(chatRoom, times(0)).upPersonCount();
        verify(chatRoomRepository, times(0)).save(chatRoom);
    }

    @Test
    @DisplayName("isFirstTimeEntry - 성공: 사용자가 채팅방에 처음 입장하는 경우")
    void isFirstTimeEntry_Success_FirstTime() {
        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        when(userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(false);

        boolean result = chatRoomService.isFirstTimeEntry(user, chatRoom);

        assertTrue(result);
        verify(userChatRoomRepository, times(1)).existsByUserAndChatRoom(user, chatRoom);
    }

    @Test
    @DisplayName("isFirstTimeEntry - 실패: 사용자가 이미 채팅방에 참여한 경우")
    void isFirstTimeEntry_Fail_AlreadyJoined() {
        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        when(userChatRoomRepository.existsByUserAndChatRoom(user, chatRoom)).thenReturn(true);

        boolean result = chatRoomService.isFirstTimeEntry(user, chatRoom);

        assertFalse(result);
        verify(userChatRoomRepository, times(1)).existsByUserAndChatRoom(user, chatRoom);
    }

    @Test
    @DisplayName("findChatRoomById - 성공: 채팅방이 존재하는 경우")
    void findChatRoomById_Success() {
        Long chatRoomId = 1L;
        ChatRoom chatRoom = mock(ChatRoom.class);
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

        ChatRoom result = chatRoomService.findChatRoomById(chatRoomId);

        assertNotNull(result);
        assertEquals(chatRoom, result);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
    }

    @Test
    @DisplayName("findChatRoomById - 실패: 채팅방이 존재하지 않는 경우")
    void findChatRoomById_Fail_NotFound() {
        Long chatRoomId = 1L;
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.findChatRoomById(chatRoomId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
    }

    @Test
    @DisplayName("findAllByUser - 성공: 사용자가 참여 중인 채팅방 목록 조회")
    void findAllByUser_Success() {
        Long userId = 1L;
        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ChatRoom chatRoom = mock(ChatRoom.class);
        when(chatRoom.getId()).thenReturn(1L);

        UserChatRoom userChatRoom = mock(UserChatRoom.class);
        when(userChatRoom.getChatRoom()).thenReturn(chatRoom);

        List<UserChatRoom> userChatRooms = List.of(userChatRoom);
        when(userChatRoomRepository.findAllByUser(user)).thenReturn(userChatRooms);

        Message message = mock(Message.class);
        when(message.getCreatedAt()).thenReturn(LocalDateTime.now());
        when(messageRepository.findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(1L, MessageType.CHAT))
                .thenReturn(Optional.of(message));

        UserChatRoomParticipationResponse response = mock(UserChatRoomParticipationResponse.class);
        when(chatRoomMapper.mapToUserChatRoomParticipationResponse(chatRoom)).thenReturn(response);

        List<UserChatRoomParticipationResponse> result = chatRoomService.findAllByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(userId);
        verify(userChatRoomRepository, times(1)).findAllByUser(user);
        verify(messageRepository, times(1)).findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(1L, MessageType.CHAT);
        verify(chatRoomMapper, times(1)).mapToUserChatRoomParticipationResponse(chatRoom);
    }

    @Test
    @DisplayName("findAllByUser - 실패: 사용자가 존재하지 않는 경우")
    void findAllByUser_Fail_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.findAllByUser(userId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(userChatRoomRepository, times(0)).findAllByUser(any());
        verify(messageRepository, times(0)).findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(anyLong(), any());
        verify(chatRoomMapper, times(0)).mapToUserChatRoomParticipationResponse(any());
    }

    @Test
    @DisplayName("findAllBriefByUser - 성공: 사용자가 참여 중인 채팅방 목록 간략히 조회")
    void findAllBriefByUser_Success() {
        Long userId = 1L;
        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ChatRoom chatRoom = mock(ChatRoom.class);
        when(chatRoom.getId()).thenReturn(1L);

        UserChatRoom userChatRoom = mock(UserChatRoom.class);
        when(userChatRoom.getChatRoom()).thenReturn(chatRoom);

        List<UserChatRoom> userChatRooms = List.of(userChatRoom);
        when(userChatRoomRepository.findAllByUser(user)).thenReturn(userChatRooms);

        Message message = mock(Message.class);
        when(message.getContent()).thenReturn("마지막 메시지 내용");
        when(messageRepository.findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(1L, MessageType.CHAT))
                .thenReturn(Optional.of(message));

        UserChatRoomBriefParticipationResponse response = mock(UserChatRoomBriefParticipationResponse.class);
        when(chatRoomMapper.mapToUserChatRoomBriefParticipationResponse(chatRoom)).thenReturn(response);

        List<UserChatRoomBriefParticipationResponse> result = chatRoomService.findAllBriefByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(userId);
        verify(userChatRoomRepository, times(1)).findAllByUser(user);
        verify(messageRepository, times(1)).findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(1L, MessageType.CHAT);
        verify(chatRoomMapper, times(1)).mapToUserChatRoomBriefParticipationResponse(chatRoom);
        verify(response, times(1)).setLastMessage("마지막 메시지 내용");
    }

    @Test
    @DisplayName("findAllBriefByUser - 실패: 사용자가 존재하지 않는 경우")
    void findAllBriefByUser_Fail_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.findAllBriefByUser(userId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(userChatRoomRepository, times(0)).findAllByUser(any());
        verify(messageRepository, times(0)).findFirstByChatRoomIdAndMessageTypeOrderByCreatedAtDesc(anyLong(), any());
        verify(chatRoomMapper, times(0)).mapToUserChatRoomBriefParticipationResponse(any());
    }

    @Test
    @DisplayName("leaveChatRoom - 성공: 사용자가 채팅방을 성공적으로 나감")
    void leaveChatRoom_Success() {
        Long userId = 1L;
        Long chatRoomId = 1L;

        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        UserChatRoom userChatRoom = mock(UserChatRoom.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)).thenReturn(Optional.of(userChatRoom));

        chatRoomService.leaveChatRoom(userId, chatRoomId);

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, times(1)).findByUserAndChatRoom(user, chatRoom);
        verify(userChatRoomRepository, times(1)).delete(userChatRoom);
        verify(chatRoom, times(1)).downPersonCount();
    }

    @Test
    @DisplayName("leaveChatRoom - 실패: 사용자 정보가 존재하지 않음")
    void leaveChatRoom_Fail_UserNotFound() {
        Long userId = 1L;
        Long chatRoomId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.leaveChatRoom(userId, chatRoomId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(0)).findById(anyLong());
        verify(userChatRoomRepository, times(0)).findByUserAndChatRoom(any(), any());
        verify(userChatRoomRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("leaveChatRoom - 실패: 채팅방 정보가 존재하지 않음")
    void leaveChatRoom_Fail_ChatRoomNotFound() {
        Long userId = 1L;
        Long chatRoomId = 1L;

        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.leaveChatRoom(userId, chatRoomId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, times(0)).findByUserAndChatRoom(any(), any());
        verify(userChatRoomRepository, times(0)).delete(any());
    }

    @Test
    @DisplayName("leaveChatRoom - 실패: 사용자가 채팅방에 참여하지 않음")
    void leaveChatRoom_Fail_UserNotInChatRoom() {
        Long userId = 1L;
        Long chatRoomId = 1L;

        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.leaveChatRoom(userId, chatRoomId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, times(1)).findByUserAndChatRoom(user, chatRoom);
        verify(userChatRoomRepository, times(0)).delete(any());
        verifyNoInteractions(chatRoom);
    }

    @Test
    @DisplayName("findMessagesByChatRoom - 성공: 채팅방 정보와 메시지 목록 조회")
    void findMessagesByChatRoom_Success() {
        Long userId = 1L;
        Long chatRoomId = 1L;

        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        UserChatRoom userChatRoom = mock(UserChatRoom.class);
        Message message = mock(Message.class);
        User chatRoomUser = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)).thenReturn(Optional.of(userChatRoom));

        when(userChatRoom.getCreatedAt()).thenReturn(LocalDateTime.now().minusHours(1));
        when(chatRoom.getId()).thenReturn(chatRoomId);
        when(chatRoom.getPersonCount()).thenReturn(5L);
        when(chatRoom.getExplanation()).thenReturn("Test Chat Room");

        List<Message> chatRoomMessages = List.of(message);
        List<UserChatRoom> chatRoomUsers = List.of(mock(UserChatRoom.class));

        when(messageRepository.findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(chatRoomId, userChatRoom.getCreatedAt()))
                .thenReturn(chatRoomMessages);
        when(userChatRoomRepository.findAllByChatRoom(chatRoom)).thenReturn(chatRoomUsers);

        when(chatRoomUsers.get(0).getUser()).thenReturn(chatRoomUser);

        List<MessageResponse> messageResponses = List.of(mock(MessageResponse.class));
        when(messageMapper.mapToMessageResponseList(chatRoomMessages)).thenReturn(messageResponses);

        List<UserChatRoomResponse> userResponses = List.of(mock(UserChatRoomResponse.class));
        when(userMapper.mapToUserChatRoomResponses(anyList())).thenReturn(userResponses);

        ChatRoomInformationResponse result = chatRoomService.findMessagesByChatRoom(userId, chatRoomId);

        assertNotNull(result);
        assertEquals(5L, result.getPersonCount());
        assertEquals("Test Chat Room", result.getExplanation());
        assertEquals(messageResponses, result.getMessageList());
        assertEquals(userResponses, result.getUserList());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, times(1)).findByUserAndChatRoom(user, chatRoom);
        verify(messageRepository, times(1))
                .findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(chatRoomId, userChatRoom.getCreatedAt());
        verify(userChatRoomRepository, times(1)).findAllByChatRoom(chatRoom);
        verify(messageMapper, times(1)).mapToMessageResponseList(chatRoomMessages);
        verify(userMapper, times(1)).mapToUserChatRoomResponses(anyList());
    }

    @Test
    @DisplayName("findMessagesByChatRoom - 실패: 사용자 정보가 존재하지 않는 경우")
    void findMessagesByChatRoom_Fail_UserNotFound() {
        Long userId = 1L;
        Long chatRoomId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.findMessagesByChatRoom(userId, chatRoomId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(0)).findById(anyLong());
        verify(userChatRoomRepository, times(0)).findByUserAndChatRoom(any(), any());
        verify(messageRepository, times(0)).findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(anyLong(), any());
        verify(userChatRoomRepository, times(0)).findAllByChatRoom(any());
        verifyNoInteractions(messageMapper, userMapper);
    }

    @Test
    @DisplayName("findMessagesByChatRoom - 실패: 채팅방 정보가 존재하지 않는 경우")
    void findMessagesByChatRoom_Fail_ChatRoomNotFound() {
        Long userId = 1L;
        Long chatRoomId = 1L;

        User user = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.findMessagesByChatRoom(userId, chatRoomId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, times(0)).findByUserAndChatRoom(any(), any());
        verify(messageRepository, times(0)).findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(anyLong(), any());
        verify(userChatRoomRepository, times(0)).findAllByChatRoom(any());
        verifyNoInteractions(messageMapper, userMapper);
    }

    @Test
    @DisplayName("findMessagesByChatRoom - 실패: 사용자가 채팅방에 참여하지 않은 경우")
    void findMessagesByChatRoom_Fail_UserNotInChatRoom() {
        Long userId = 1L;
        Long chatRoomId = 1L;

        User user = mock(User.class);
        ChatRoom chatRoom = mock(ChatRoom.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));
        when(userChatRoomRepository.findByUserAndChatRoom(user, chatRoom)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class, () -> chatRoomService.findMessagesByChatRoom(userId, chatRoomId));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findById(chatRoomId);
        verify(userChatRoomRepository, times(1)).findByUserAndChatRoom(user, chatRoom);
        verify(messageRepository, times(0)).findByChatRoomIdAndCreatedAtAfterOrderByCreatedAtAsc(anyLong(), any());
        verify(userChatRoomRepository, times(0)).findAllByChatRoom(any());
        verifyNoInteractions(messageMapper, userMapper);
    }

    @Test
    @DisplayName("findAllChatRoomBySearchAndSort - 성공: 검색 및 정렬에 따른 채팅방 목록 반환")
    void findAllChatRoomBySearchAndSort_Success() {
        Long userId = 1L;
        String zoneCategoryName = "TestZone";
        String sort = "popular";
        String search = "TestSearch";

        User user = mock(User.class);
        ZoneCategory zoneCategory = mock(ZoneCategory.class);
        ChatRoom chatRoom = mock(ChatRoom.class);
        List<ChatRoom> chatRooms = List.of(chatRoom);
        List<ChatRoomBriefResponse> chatRoomBriefResponses = List.of(mock(ChatRoomBriefResponse.class));

        when(zoneCategoryRepository.findByName(zoneCategoryName)).thenReturn(Optional.of(zoneCategory));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(chatRoomRepository.findChatRoomsByZoneCategoryAndNotJoinedByUser(eq(zoneCategory), eq(user), eq(search), any(Sort.class)))
                .thenReturn(chatRooms);
        when(chatRoomMapper.mapToChatRoomBriefResponseList(chatRooms)).thenReturn(chatRoomBriefResponses);

        List<ChatRoomBriefResponse> result = chatRoomService.findAllChatRoomBySearchAndSort(userId, zoneCategoryName, sort, search);

        assertNotNull(result);
        assertEquals(chatRoomBriefResponses.size(), result.size());
        assertEquals(chatRoomBriefResponses, result);

        verify(zoneCategoryRepository, times(1)).findByName(zoneCategoryName);
        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(1)).findChatRoomsByZoneCategoryAndNotJoinedByUser(eq(zoneCategory), eq(user), eq(search), any(Sort.class));
        verify(chatRoomMapper, times(1)).mapToChatRoomBriefResponseList(chatRooms);
    }

    @Test
    @DisplayName("findAllChatRoomBySearchAndSort - 실패: ZoneCategory가 존재하지 않는 경우")
    void findAllChatRoomBySearchAndSort_Fail_ZoneCategoryNotFound() {
        Long userId = 1L;
        String zoneCategoryName = "InvalidZone";
        when(zoneCategoryRepository.findByName(zoneCategoryName)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class,
                () -> chatRoomService.findAllChatRoomBySearchAndSort(userId, zoneCategoryName, "recent", "test"));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(zoneCategoryRepository, times(1)).findByName(zoneCategoryName);
        verify(userRepository, times(0)).findById(anyLong());
        verify(chatRoomRepository, times(0)).findChatRoomsByZoneCategoryAndNotJoinedByUser(any(), any(), any(), any());
        verifyNoInteractions(chatRoomMapper);
    }

    @Test
    @DisplayName("findAllChatRoomBySearchAndSort - 실패: User가 존재하지 않는 경우")
    void findAllChatRoomBySearchAndSort_Fail_UserNotFound() {
        Long userId = 1L;
        String zoneCategoryName = "TestZone";

        ZoneCategory zoneCategory = mock(ZoneCategory.class);
        when(zoneCategoryRepository.findByName(zoneCategoryName)).thenReturn(Optional.of(zoneCategory));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException exception = assertThrows(ApiException.class,
                () -> chatRoomService.findAllChatRoomBySearchAndSort(userId, zoneCategoryName, "recent", "test"));
        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());

        verify(zoneCategoryRepository, times(1)).findByName(zoneCategoryName);
        verify(userRepository, times(1)).findById(userId);
        verify(chatRoomRepository, times(0)).findChatRoomsByZoneCategoryAndNotJoinedByUser(any(), any(), any(), any());
        verifyNoInteractions(chatRoomMapper);
    }


}