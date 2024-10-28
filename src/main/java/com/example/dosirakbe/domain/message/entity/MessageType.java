package com.example.dosirakbe.domain.message.entity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import lombok.Getter;

@Getter
public enum MessageType {
    CHAT, JOIN;

    public static Optional<MessageType> fromString(String type) {
        if (Objects.isNull(type) || type.isBlank()) {
            return Optional.empty();
        }

        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(type))
                .findFirst();
    }
}