package com.example.dosirakbe.config;

import com.example.dosirakbe.domain.chat_room.entity.ChatRoom;
import com.example.dosirakbe.domain.chat_room.repository.ChatRoomRepository;
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

        // STOMP 헤더에서 유저 정보와 채팅방 ID 추출 (headers에서 가져옴)
        String userIdHeader = headerAccessor.getFirstNativeHeader("userId"); // 클라이언트에서 전달된 유저 정보
        String chatRoomIdHeader = headerAccessor.getFirstNativeHeader("chatRoomId");

        if (Objects.isNull(chatRoomIdHeader) || Objects.isNull(userIdHeader)) {
            throw new RuntimeException(""); //TODO
        }

        Long userId = Long.parseLong(userIdHeader);
        Long chatRoomId = Long.parseLong(chatRoomIdHeader);

        ChatRoom chatRoom = chatRoomService.findChatRoomById(chatRoomId);

        User user = userRepository.findById(userId).orElseThrow();

        // 유저가 처음 입장했는지 확인
        if (chatRoomService.isFirstTimeEntry(user, chatRoom)) {
            chatRoomService.joinChatRoom(user, chatRoom);

            // 입장 메시지 전송
            MessageResponse firstJoinMessage = messageService.firstJoinMessage(user, chatRoom);

            messagingTemplate.convertAndSend("/topic/chat-room/" + firstJoinMessage.getChatRoomId(), firstJoinMessage);
        }


    }

    @EventListener
    public void handleSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String userIdHeader = headerAccessor.getFirstNativeHeader("userId"); // 클라이언트에서 전달된 유저 정보
        String chatRoomIdHeader = headerAccessor.getFirstNativeHeader("chatRoomId");

        if (Objects.isNull(chatRoomIdHeader) || Objects.isNull(userIdHeader)) {
            throw new RuntimeException(""); //TODO
        }

        Long userId = Long.parseLong(userIdHeader);
        Long chatRoomId = Long.parseLong(chatRoomIdHeader);

        if (!chatRoomRepository.existsById(chatRoomId)) {
            throw new RuntimeException(""); //TODO
        }

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException(""); //TODO
        }


    }
}