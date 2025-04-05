package com.example.dosirakbe.domain.user;

import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;

/**
 * packageName    : com.example.dosirakbe.global.util<br>
 * fileName       : null.java<br>
 * author         : SSAFY<br>
 * date           : 2025-04-04<br>
 * description    :  <br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * <br>
 * <br>
 */
public final class UserUtility {


    /**
     * 인증된 사용자의 ID를 추출합니다.
     *
     * <p>
     * 이 메서드는 인증된 {@link CustomOAuth2User} 객체로부터 사용자의 ID를 추출하여 반환합니다.
     * </p>
     *
     * @param customOAuth2User 인증된 사용자 정보
     * @return 사용자의 고유 ID
     */
    public static Long getUserIdByOAuth(CustomOAuth2User customOAuth2User) {
        return customOAuth2User.getUserDTO().getUserId();
    }

    private UserUtility() {
    }
}
