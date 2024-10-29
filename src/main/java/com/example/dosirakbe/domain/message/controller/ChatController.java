package com.example.dosirakbe.domain.message.controller;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
import com.example.dosirakbe.domain.message.dto.request.MessageRegisterRequest;
import com.example.dosirakbe.domain.message.dto.response.MessageResponse;
import com.example.dosirakbe.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final MessageService messageService;


    @MessageMapping("/chat-room/{chatRoomId}/sendMessage")
    @SendTo("/topic/chat-room/{chatRoomId}")
    public MessageResponse sendMessage(@AuthenticationPrincipal CustomOAuth2User customOAuth2User, @DestinationVariable Long chatRoomId, MessageRegisterRequest messageRegisterRequest) {
        Long userId = customOAuth2User.getUserDTO().getUserId();
        MessageResponse message = messageService.createMessage(userId, chatRoomId, messageRegisterRequest);

        return message;
    }
}
