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
import com.example.dosirakbe.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final JwtUtil jwtUtil;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        try {
            // STOMP 헤더에서 유저 정보와 채팅방 ID 추출 (headers에서 가져옴)
            Long userId = validationAuthorization(headerAccessor);
            String chatRoomIdHeader = headerAccessor.getFirstNativeHeader("chatRoomId");
            if (Objects.isNull(chatRoomIdHeader)) {
                throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
            }

            Long chatRoomId = Long.parseLong(chatRoomIdHeader);

            ChatRoom chatRoom = chatRoomService.findChatRoomById(chatRoomId);

            User user = userRepository.findById(userId)
                    .orElseThrow(
                            () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND));

            if (chatRoomService.isFirstTimeEntry(user, chatRoom)) {
                chatRoomService.joinChatRoom(user, chatRoom);

                MessageResponse firstJoinMessage = messageService.firstJoinMessage(user, chatRoom);

                messagingTemplate.convertAndSend("/topic/chat-room/" + firstJoinMessage.getChatRoomId(), firstJoinMessage);
            }
        } catch (Exception e) {
            sendErrorMessageToUser(headerAccessor, e.getMessage());
        }


    }



    private Long validationAuthorization(StompHeaderAccessor headerAccessor) {
        String authorizationHeader = headerAccessor.getFirstNativeHeader("Authorization");

        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            try {
                return jwtUtil.getUserId(token);
            } catch (Exception e) {
                throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
            }
        }

        throw new ApiException(ExceptionEnum.ACCESS_DENIED_EXCEPTION);
    }


    private void sendErrorMessageToUser(StompHeaderAccessor headerAccessor, String errorMessage) {
        String body = "{\"error\": \"" + errorMessage + "\"}";
        String sessionId = headerAccessor.getSessionId();
        if (sessionId != null) {
            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    "/queue/errors",
                    body
            );
        }
    }
}