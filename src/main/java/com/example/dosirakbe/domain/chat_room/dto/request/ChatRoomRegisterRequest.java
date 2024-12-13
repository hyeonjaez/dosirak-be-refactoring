package com.example.dosirakbe.domain.chat_room.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : com.example.dosirakbe.domain.chat_room.dto.request<br>
 * fileName       : ChatRoomRegisterRequest<br>
 * author         : Fiat_lux<br>
 * date           : 10/23/24<br>
 * description    : 채팅방 생성 요청을 위한 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/23/24        Fiat_lux                최초 생성<br>
 */
@Getter
@NoArgsConstructor
public class ChatRoomRegisterRequest {

    /**
     * 채팅방의 제목입니다.
     *
     * <p>
     * 제목은 필수 입력 사항이며, 1자 이상 50자 이하로 제한됩니다.
     * </p>
     *
     * @see NotBlank
     * @see Size
     */
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;

    /**
     * 채팅방의 설명입니다.
     *
     * <p>
     * 설명은 필수 입력 사항이며, 최소 1자 이상이어야 합니다.
     * </p>
     *
     * @see NotBlank
     * @see Size
     */
    @NotBlank
    @Size(min = 1)
    private String explanation;

    /**
     * 채팅방이 속한 지역 카테고리의 이름입니다.
     *
     * <p>
     * 지역 카테고리 이름은 필수 입력 사항이며, 1자 이상 20자 이하로 제한됩니다.
     * </p>
     *
     * @see NotBlank
     * @see Size
     */
    @NotBlank
    @Size(min = 1, max = 20)
    private String zoneCategoryName;

    /**
     * 채팅방의 기본 이미지 URL 입니다.
     *
     * <p>
     * 기본 이미지는 선택 사항이며, 지정되지 않은 경우 기본값을 사용합니다.
     * </p>
     */
    private String defaultImage;
}
