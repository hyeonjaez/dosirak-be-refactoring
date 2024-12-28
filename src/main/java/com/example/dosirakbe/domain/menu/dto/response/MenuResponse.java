package com.example.dosirakbe.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : com.example.dosirakbe.domain.menu.dto.response<br>
 * fileName       : MenuResponse<br>
 * author         : yyujin1231<br>
 * date           : 10/25/24<br>
 * description    : 메뉴 정보를 클라이언트에 전달하기 위한 응답 DTO 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 10/25/24        yyujin1231                최초 생성<br>
 */


@Getter
@AllArgsConstructor
public class MenuResponse {

    /**
     * 메뉴의 고유 식별자입니다.
     *
     * <p>
     * 이 필드는 메뉴의 고유 ID를 나타냅니다.
     * </p>
     */
    private Long menuId;

    /**
     * 메뉴의 이름입니다.
     *
     * <p>
     * 이 필드는 메뉴의 이름을 나타냅니다.
     * </p>
     */
    private String menuName;

    /**
     * 메뉴의 이미지를 나타냅니다.
     *
     * <p>
     * 이 필드는 메뉴의 이미지를 저장하는 URL 경로를 포함합니다.
     * </p>
     */
    private String menuImg;

    /**
     * 메뉴의 가격입니다.
     *
     * <p>
     * 이 필드는 메뉴의 가격 정보를 포함합니다.
     * </p>
     */
    private Integer menuPrice;

    /**
     * 다회용기 추천용량입니다.
     *
     * <p>
     * 이 필드는 GPT API를 활용하여 반환된 추천용량값을 나타냅니다.
     * </p>
     */
    private String menuPackSize;
}