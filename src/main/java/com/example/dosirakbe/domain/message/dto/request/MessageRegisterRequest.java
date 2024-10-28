package com.example.dosirakbe.domain.message.dto.request;

import com.example.dosirakbe.domain.message.entity.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRegisterRequest {

    @NotBlank
    private String content;

    @NotNull
    private MessageType messageType;
}
