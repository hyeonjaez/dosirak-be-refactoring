package com.example.dosirakbe.config;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.service.ChatRoomService;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.service.MessageService;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.global.config<br>
 * fileName       : WebSocketEventListener<br>
 * author         : Fiat_lux<br>
 * date           : 10/19/24<br>
 * description    : WebSocket 연결 이벤트를 처리하여 사용자 참여 및 메시지 전송을 관리하는 리스너 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/19/24        Fiat_lux                최초 생성<br>
 */
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;
    private final JwtUtil jwtUtil;

    /**
     * WebSocket 연결 이벤트를 처리하여 사용자의 채팅방 참여 및 초기 메시지를 전송합니다.
     *
     * <p>
     * 이 메서드는 클라이언트가 WebSocket 에 연결할 때 발생하는 {@link SessionConnectEvent}를 수신하여,
     * 연결된 사용자의 인증 정보를 검증하고, 지정된 채팅방에 참여하게 하며,
     * 채팅방에 처음 참여하는 경우 초기 메시지를 전송합니다.
     * </p>
     *
     * @param event WebSocket 연결 이벤트를 나타내는 {@link SessionConnectEvent} 객체
     */
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

    /**
     * STOMP 헤더에서 JWT 토큰을 추출하고 유효성을 검증하여 사용자 ID를 반환합니다.
     *
     * <p>
     * 이 메서드는 STOMP 헤더에서 "Authorization" 헤더를 추출하고, "Bearer " 접두사가 있는지 확인한 후,
     * JWT 토큰을 유효성 검증하여 사용자 ID를 반환합니다.
     * 유효하지 않은 토큰이거나 헤더가 누락된 경우 {@link ApiException}을 던집니다.
     * </p>
     *
     * @param headerAccessor STOMP 헤더 정보를 담고 있는 {@link StompHeaderAccessor} 객체
     * @return 인증된 사용자의 고유 식별자 {@link Long}
     * @throws ApiException 인증 실패 시 발생
     */
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

    /**
     * 사용자에게 오류 메시지를 전송합니다.
     *
     * <p>
     * 이 메서드는 WebSocket 세션에 오류 메시지를 전송하여 클라이언트에게 알립니다.
     * 세션 ID가 존재할 경우, 해당 세션의 "/queue/errors" 큐로 오류 메시지를 전송합니다.
     * </p>
     *
     * @param headerAccessor 오류 메시지를 전송할 WebSocket 세션의 {@link StompHeaderAccessor} 객체
     * @param errorMessage 전송할 오류 메시지 내용 {@link String}
     */
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