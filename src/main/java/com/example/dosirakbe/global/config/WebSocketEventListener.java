package com.example.dosirakbe.global.config;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.repository.ChatRoomRepository;
import com.example.dosirakbe.domain.chat_room.service.ChatRoomService;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.service.MessageService;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final ChatRoomRepository chatRoomRepository;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        try {
            // STOMP 헤더에서 유저 정보와 채팅방 ID 추출 (headers에서 가져옴)
            String chatRoomIdHeader = headerAccessor.getFirstNativeHeader("chatRoomId");
            Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");

            if (Objects.isNull(chatRoomIdHeader) || Objects.isNull(userId)) {
                throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
            }

            Long chatRoomId = Long.parseLong(chatRoomIdHeader);

            ChatRoom chatRoom = chatRoomService.findChatRoomById(chatRoomId);

            User user = userRepository.findById(userId)
                    .orElseThrow(
                            () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

            // 유저가 처음 입장했는지 확인
            if (chatRoomService.isFirstTimeEntry(user, chatRoom)) {
                chatRoomService.joinChatRoom(user, chatRoom);

                // 입장 메시지 전송
                MessageResponse firstJoinMessage = messageService.firstJoinMessage(user, chatRoom);

                messagingTemplate.convertAndSend("/topic/chat-room/" + firstJoinMessage.getChatRoomId(), firstJoinMessage);
            }
        } catch (Exception e) {
            sendErrorMessageToUser(headerAccessor, e.getMessage());
        }


    }


    @EventListener
    public void handleSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        try {
            String chatRoomIdHeader = headerAccessor.getFirstNativeHeader("chatRoomId");
            Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
            if (Objects.isNull(chatRoomIdHeader) || Objects.isNull(userId)) {
                throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
            }

            Long chatRoomId = Long.parseLong(chatRoomIdHeader);

            if (!chatRoomRepository.existsById(chatRoomId)) {
                throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
            }

            if (!userRepository.existsById(userId)) {
                throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
            }
        } catch (Exception e) {
            sendErrorMessageToUser(headerAccessor, e.getMessage());
        }


    }

    private void sendErrorMessageToUser(StompHeaderAccessor headerAccessor, String errorMessage) {
        Object userIdObj = (headerAccessor.getSessionAttributes() != null) ? headerAccessor.getSessionAttributes().get("userId") : null;

        if (userIdObj instanceof Long) {
            Long userId = (Long) userIdObj;
            messagingTemplate.convertAndSendToUser(
                    userId.toString(), // userId를 문자열로 변환
                    "/queue/errors",
                    errorMessage
            );
        }
    }
}