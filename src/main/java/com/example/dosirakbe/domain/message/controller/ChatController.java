package com.example.dosirakbe.domain.message.controller;


import com.example.dosirakbe.domain.message.dto.request.MessageRegisterRequest;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.service.MessageService;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.domain.message.controller<br>
 * fileName       : ChatController<br>
 * author         : Fiat_lux<br>
 * date           : 10/20/24<br>
 * description    : 실시간 채팅 메시지를 처리하는 컨트롤러 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        Fiat_lux                최초 생성<br>
 */
@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    /**
     * 실시간 채팅 메시지를 수신하고 처리하는 메서드입니다.
     *
     * <p>
     * 이 메서드는 지정된 채팅 방 ID에 대한 메시지를 수신하고,
     * 메시지를 처리한 후 해당 채팅 방의 모든 구독자에게 메시지를 전송합니다.
     * </p>
     *
     * @param chatRoomId     메시지를 전송할 채팅 방의 고유 ID
     * @param messagePayLoad 수신한 메시지의 페이로드(JSON 형식)
     * @param headerAccessor 메시지 헤더에 접근할 수 있는 {@link SimpMessageHeaderAccessor} 객체
     * @return 처리된 메시지를 포함한 {@link MessageResponse} 객체
     * @throws ApiException {@link ExceptionEnum#INVALID_REQUEST} 예외 발생 시
     */
    @MessageMapping("/chat-room/{chatRoomId}/sendMessage")
    @SendTo("/topic/chat-room/{chatRoomId}")
    public MessageResponse sendMessage(@DestinationVariable Long chatRoomId,
                                       String messagePayLoad,
                                       SimpMessageHeaderAccessor headerAccessor) {

        MessageRegisterRequest messageRegisterRequest;

        try {
            messageRegisterRequest = objectMapper.readValue(messagePayLoad, MessageRegisterRequest.class);
        } catch (Exception e) {
            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
        }

        Long userId = validationAuthorization(headerAccessor);

        return messageService.createMessage(userId, chatRoomId, messageRegisterRequest);
    }

    /**
     * 인증 헤더를 검증하고 사용자 ID를 추출하는 메서드입니다.
     *
     * <p>
     * 이 메서드는 메시지 헤더에서 "Authorization" 헤더를 추출하고,
     * JWT 토큰을 검증하여 사용자 ID를 반환합니다.
     * </p>
     *
     * @param headerAccessor 메시지 헤더에 접근할 수 있는 {@link SimpMessageHeaderAccessor} 객체
     * @return 인증된 사용자의 고유 ID
     * @throws ApiException {@link ExceptionEnum#ACCESS_DENIED_EXCEPTION} 예외 발생 시
     */
    private Long validationAuthorization(SimpMessageHeaderAccessor headerAccessor) {
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
}
