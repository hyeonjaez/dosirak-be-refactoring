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

/**
 * packageName    : com.example.dosirakbe.domain.message.service<br>
 * fileName       : MessageService<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 메시지 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
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

    /**
     * 새로운 메시지를 생성하고 저장한 후, 해당 메시지를 {@link MessageResponse} Response DTO 로 변환하여 반환합니다.
     *
     * <p>
     * 이 메서드는 사용자의 ID와 채팅 방의 ID를 기반으로 사용자가 해당 채팅 방에 속해 있는지 검증하고,
     * 메시지를 생성하여 저장한 후, 매퍼를 통해 DTO 로 변환하여 반환합니다.
     * </p>
     *
     * @param userId                 메시지를 생성하는 사용자의 고유 식별자
     * @param chatRoomId             메시지가 전송될 채팅 방의 고유 식별자
     * @param messageRegisterRequest 메시지 생성에 필요한 요청 정보 {@link MessageRegisterRequest}
     * @return 생성된 메시지를 포함하는 {@link MessageResponse} DTO 객체
     * @throws ApiException {@link ExceptionEnum#DATA_NOT_FOUND} 또는 {@link ExceptionEnum#RUNTIME_EXCEPTION} 예외 발생 시
     */
    public MessageResponse createMessage(Long userId, Long chatRoomId, MessageRegisterRequest messageRegisterRequest) {
        User user = userRepository.findById(userId)
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

    /**
     * 사용자가 채팅 방에 처음 참여할 때 생성되는 JOIN 메시지를 생성하고 저장한 후, 해당 메시지를 {@link MessageResponse} Response DTO 로 변환하여 반환합니다.
     *
     * <p>
     * 이 메서드는 사용자의 닉네임과 정해진 JOIN 메시지를 결합하여 메시지를 생성하고, 저장한 후 Response DTO 로 변환하여 반환합니다.
     * </p>
     *
     * @param user     메시지를 생성하는 사용자 {@link User}
     * @param chatRoom 메시지가 전송될 채팅 방 {@link ChatRoom}
     * @return 생성된 JOIN 메시지를 포함하는 {@link MessageResponse} Response DTO 객체
     */
    public MessageResponse firstJoinMessage(User user, ChatRoom chatRoom) {
        Message message = new Message(user.getNickName() + FIRST_JOIN_MESSAGE, MessageType.JOIN, user, chatRoom);
        Message saveMessage = messageRepository.save(message);

        return messageMapper.mapToMessageResponse(saveMessage);
    }
}
