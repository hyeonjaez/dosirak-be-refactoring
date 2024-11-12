package com.example.dosirakbe.domain.message.controller;


import com.example.dosirakbe.domain.message.dto.request.MessageRegisterRequest;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.service.MessageService;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import com.example.dosirakbe.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;
    private final JwtUtil jwtUtil;


    @MessageMapping("/chat-room/{chatRoomId}/sendMessage")
    @SendTo("/topic/chat-room/{chatRoomId}")
    public MessageResponse sendMessage(@DestinationVariable Long chatRoomId,
                                       MessageRegisterRequest messageRegisterRequest,
                                       SimpMessageHeaderAccessor headerAccessor) {
        Long userId = validationAuthorization(headerAccessor);
        MessageResponse message = messageService.createMessage(userId, chatRoomId, messageRegisterRequest);

        return message;
    }

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
