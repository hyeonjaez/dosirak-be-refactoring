package com.example.dosirakbe.domain.message.entity;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.domain.message.entity<br>
 * fileName       : MessageType<br>
 * author         : Fiat_lux<br>
 * date           : 10/17/24<br>
 * description    : 메시지의 유형을 정의하는 열거형 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/17/24        Fiat_lux                최초 생성<br>
 */
@Getter
public enum MessageType {
    /**
     * 채팅 메시지 유형을 나타냅니다.
     *
     * <p>
     * 사용자가 일반 채팅 메시지를 전송할 때 사용됩니다.
     * </p>
     */
    CHAT,

    /**
     * 사용자가 채팅 방에 참여할 때 사용하는 메시지 유형입니다.
     *
     * <p>
     * 사용자가 새로운 채팅 방에 참여하거나 기존 방에 재참여할 때 사용됩니다.
     * </p>
     */
    JOIN;

    /**
     * 주어진 문자열을 {@link MessageType}으로 변환합니다.
     *
     * <p>
     * 이 메서드는 입력된 문자열과 일치하는 {@link MessageType}을 찾아 {@link Optional}로 반환합니다.
     * 대소문자를 구분하지 않으며, 일치하는 유형이 없을 경우 {@link Optional#empty()}를 반환합니다.
     * </p>
     *
     * @param type 변환할 문자열 타입
     * @return 일치하는 {@link MessageType}을 포함하는 {@link Optional}, 일치하지 않으면 {@link Optional#empty()}
     */
    public static Optional<MessageType> fromString(String type) {
        if (Objects.isNull(type) || type.isBlank()) {
            return Optional.empty();
        }

        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(type))
                .findFirst();
    }
}