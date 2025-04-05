//package com.example.dosirakbe.domain.auth.service;
//
//import com.example.dosirakbe.domain.auth.dto.*;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.*;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//
///**
// * packageName    : com.example.dosirakbe.domain.auth.service<br>
// * fileName       : CustomOAuth2UserService<br>
// * author         : yyujin1231<br>
// * date           : 10/20/24<br>
// * description    : 외부 OAuth2 제공자로부터 사용자 정보를 가져오는 로직을 처리하는 서비스 클래스입니다.<br>
// * ===========================================================<br>
// * DATE              AUTHOR             NOTE<br>
// * -----------------------------------------------------------<br>
// * 10/20/24        yyujin1231                최초 생성<br>
// */
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    /**
//     * <p>Spring Security의 {@link DefaultOAuth2UserService}를 상속받아 구현되었으며,
//     * RestTemplate을 사용하여 외부 API 호출을 수행합니다.</p>
//     */
//
//    private final RestTemplate restTemplate;
//
//    /**
//     * 카카오 액세스 토큰을 처리하여 사용자 정보를 반환합니다.
//     *
//     * @param kakaoAccessToken 카카오에서 발급된 액세스 토큰
//     * @return {@link KakaoUserInfo} 객체, 사용자 정보를 포함합니다.
//     */
//
//
//    public KakaoUserInfo processKakaoToken(String kakaoAccessToken) {
//        return getUserInfoFromKakao(kakaoAccessToken);
//    }
//
//    /**
//     * 카카오 API를 호출하여 사용자 정보를 가져옵니다.
//     *
//     * @param accessToken 카카오에서 발급된 액세스 토큰
//     * @return {@link KakaoUserInfo} 객체, 사용자 정보를 포함합니다.
//     * @throws ApiException {@link ExceptionEnum#INVALID_ACCESS_TOKEN} 인증 오류 발생 시
//     * @throws ApiException {@link ExceptionEnum#RUNTIME_EXCEPTION} 기타 예외 발생 시
//     */
//    private KakaoUserInfo getUserInfoFromKakao(String accessToken) {
//        String url = "https://kapi.kakao.com/v2/user/me";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
//            return new KakaoUserInfo(response.getBody());
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                throw new ApiException(ExceptionEnum.INVALID_ACCESS_TOKEN);
//            }
//            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
//        }
//    }
//
//
//    /**
//     * 네이버 액세스 토큰을 처리하여 사용자 정보를 반환합니다.
//     *
//     * @param naverAccessToken 네이버에서 발급된 액세스 토큰
//     * @return {@link NaverUserInfo} 객체, 사용자 정보를 포함합니다.
//     */
//
//    public NaverUserInfo processNaverToken(String naverAccessToken) {
//        return getUserInfoFromNaver(naverAccessToken);
//    }
//
//    /**
//     * 네이버 API를 호출하여 사용자 정보를 가져옵니다.
//     *
//     * @param accessToken 네이버에서 발급된 액세스 토큰
//     * @return {@link NaverUserInfo} 객체, 사용자 정보를 포함합니다.
//     * @throws ApiException {@link ExceptionEnum#INVALID_ACCESS_TOKEN} 인증 오류 발생 시
//     * @throws ApiException {@link ExceptionEnum#RUNTIME_EXCEPTION} 기타 예외 발생 시
//     */
//
//    private NaverUserInfo getUserInfoFromNaver(String accessToken) {
//        String url = "https://openapi.naver.com/v1/nid/me";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(accessToken);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        try {
//            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
//            return new NaverUserInfo(response.getBody());
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                throw new ApiException(ExceptionEnum.INVALID_ACCESS_TOKEN);
//            }
//            throw new ApiException(ExceptionEnum.RUNTIME_EXCEPTION);
//        }
//    }
//
//
//}