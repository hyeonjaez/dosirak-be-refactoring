package com.example.dosirakbe.domain.message.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class MessageTypeConverter implements AttributeConverter<MessageType, String> {

    @Override
    public String convertToDatabaseColumn(MessageType messageType) {
        if (Objects.isNull(messageType)) {
            return null;
        }
        return messageType.name();
    }

    @Override
    public MessageType convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData) || dbData.isBlank()) {
            return null;
        }
        try {
            return MessageType.valueOf(dbData.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unknown value for MessageType: " + dbData, e);
        }
    }
}