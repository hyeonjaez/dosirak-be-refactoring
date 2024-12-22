package com.example.dosirakbe.domain.message.dto.request;

import com.example.dosirakbe.domain.message.entity.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : com.example.dosirakbe.domain.message.dto.request<br>
 * fileName       : MessageRegisterRequest<br>
 * author         : Fiat_lux<br>
 * date           : 10/20/24<br>
 * description    : 메시지 생성 요청을 위한 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/20/24        Fiat_lux                최초 생성<br>
 * 12/23/24        Fiat_lux               필드에 json 어노테이션 추가<br>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRegisterRequest {

    /**
     * 메시지의 내용입니다.
     *
     * <p>
     * 이 필드는 메시지의 실제 내용을 담으며, 빈 문자열일 수 없습니다.
     * </p>
     */
    @NotBlank
    private String content;

    /**
     * 메시지의 유형을 나타냅니다.
     *
     * <p>
     * 이 필드는 {@link MessageType} 열거형을 사용하여 메시지의 종류를 정의하며, 반드시 값이 존재해야 합니다.
     * </p>
     */
    @NotNull
    @JsonProperty("message_type")
    private MessageType messageType;
}
