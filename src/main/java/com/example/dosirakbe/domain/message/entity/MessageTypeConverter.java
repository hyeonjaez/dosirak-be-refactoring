package com.example.dosirakbe.domain.message.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

/**
 * packageName    : com.example.dosirakbe.domain.message.entity<br>
 * fileName       : MessageTypeConverter<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : {@link MessageType} 열거형을 데이터베이스 컬럼으로 변환하거나<br>
 * 데이터베이스 컬럼 값을 {@link MessageType} 열거형으로 변환하는 컨버터 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
@Converter(autoApply = true)
public class MessageTypeConverter implements AttributeConverter<MessageType, String> {

    /**
     * {@link MessageType} 열거형을 데이터베이스에 저장할 문자열로 변환합니다.
     *
     * <p>
     * 이 메서드는 {@link MessageType} 열거형 값을 데이터베이스에 저장하기 적합한 문자열 형식으로 변환합니다.
     * {@code null} 값은 데이터베이스에 {@code null}로 저장됩니다.
     * </p>
     *
     * @param messageType 변환할 {@link MessageType} 열거형 값
     * @return 데이터베이스에 저장할 문자열 값, {@code messageType}이 {@code null}인 경우 {@code null}
     */
    @Override
    public String convertToDatabaseColumn(MessageType messageType) {
        if (Objects.isNull(messageType)) {
            return null;
        }
        return messageType.name();
    }

    /**
     * 데이터베이스에 저장된 문자열을 {@link MessageType} 열거형으로 변환합니다.
     *
     * <p>
     * 이 메서드는 데이터베이스에서 읽은 문자열 값을 {@link MessageType} 열거형으로 변환합니다.
     * 문자열이 {@code null}이거나 빈 문자열인 경우 {@code null}을 반환합니다.
     * 문자열이 유효한 {@link MessageType} 값이 아닌 경우 {@link IllegalStateException}을 던집니다.
     * </p>
     *
     * @param dbData 데이터베이스에서 읽은 문자열 값
     * @return 변환된 {@link MessageType} 열거형 값, {@code dbData}가 {@code null}이거나 빈 문자열인 경우 {@code null}
     * @throws IllegalStateException {@code dbData}가 유효한 {@link MessageType} 값이 아닌 경우 발생
     */
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