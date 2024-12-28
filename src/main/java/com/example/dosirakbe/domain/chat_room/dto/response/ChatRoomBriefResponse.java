package com.example.dosirakbe.domain.chat_room.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.dto.response<br>
 * fileName       : ChatRoomBriefResponse<br>
 * author         : Fiat_lux<br>
 * date           : 10/23/24<br>
 * description    : 채팅방의 간략한 정보를 전달하기 위한 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/23/24        Fiat_lux                최초 생성<br>
 * 12/23/24        Fiat_lux               필드에 json 어노테이션 추가<br>
 */
@Getter
@AllArgsConstructor
public class ChatRoomBriefResponse {

    /**
     * 채팅방의 고유 식별자입니다.
     */
    private Long id;

    /**
     * 채팅방의 제목입니다.
     */
    private String title;

    /**
     * 채팅방의 대표 이미지 URL 입니다.
     *
     * <p>
     * 채팅방의 시각적 표현을 위한 이미지 URL 을 포함합니다.
     * </p>
     */
    private String image;

    /**
     * 채팅방에 현재 참여하고 있는 사람의 수입니다.
     */
    @JsonProperty("person_count")
    private Long personCount;

    /**
     * 채팅방의 간단한 설명입니다.
     */
    private String explanation;
}
