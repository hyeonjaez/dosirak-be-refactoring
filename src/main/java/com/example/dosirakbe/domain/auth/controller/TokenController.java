//package com.example.dosirakbe.domain.auth.controller;
//
//
//import com.example.dosirakbe.domain.auth.dto.response.TokenResponse;
//import com.example.dosirakbe.domain.auth.service.TokenService;
//import com.example.dosirakbe.global.util.JwtUtil;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.oauth2.jwt.JwtException;
//import org.springframework.web.bind.annotation.*;
//
///**
// * packageName    : com.example.dosirakbe.domain.auth.controller<br>
// * fileName       : TokenController<br>
// * author         : yyujin1231<br>
// * date           : 10/20/24<br>
// * description    : 토큰 제공 관련 CRUD controller 클래스 입니다.<br>
// * ===========================================================<br>
// * DATE              AUTHOR             NOTE<br>
// * -----------------------------------------------------------<br>
// * 10/20/24        yyujin1231                최초 생성<br>
// */
//
//
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/tokens")
//public class TokenController {
//
//    private final TokenService tokenService;
//    private final JwtUtil jwtUtil;
//
//    /**
//     * 액세스 토큰을 재발급합니다.
//     *
//     * <p>
//     * 이 메서드는 유효한 토큰을 활용하여,새로운 토큰을 재발급합니다.
//     * {@link ApiResult} 형태로 재발급된 토큰 정보를 반환합니다.
//     * </p>
//
//     * @return 재발급된 토큰 정보를 포함하는 {@link ApiResult} 형태의 {@link TokenResponse} 객체
//     *
//     * @throws JwtException 토큰 발급시 오류가 발생한 경우
//     */
//
//    @GetMapping
//    public ResponseEntity<ApiResult> reissueAccessToken() {
//        try {
//
//            TokenResponse accessToken = tokenService.reissueAccessToken();
//
//            ApiResult<TokenResponse> result = ApiResult.<TokenResponse>builder()
//                    .status(StatusEnum.SUCCESS)
//                    .message("토큰 재발급에 성공하였습니다")
//                    .data(accessToken)
//                    .exception(null)
//                    .build();
//
//            return new ResponseEntity<>(result, HttpStatus.OK);
//
//        } catch (JwtException e) {
//
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
//                    ApiResult.builder()
//                            .status(StatusEnum.FAILURE)
//                            .message("토큰 재발급에 실패하였습니다")
//                            .build()
//            );
//
//        }
//    }
//
//
//    /**
//     * 토큰 유효성 검증 결과를 반환합니다.
//     *
//     * <p>
//     * 이 메서드는 Authorization 헤더에 포함된 토큰을 추출하고, 유효성을 검증한 후
//     * 검증 결과에 따라 성공 또는 실패 메시지를 반환합니다.
//     * </p>
//
//     * @param authorizationHeader 클라이언트 요청에서 전달된 Authorization 헤더
//     * @return 토큰 검증 결과를 포함하는 {@link ApiResult} 객체
//     *
//     *
//     *  @throws IllegalArgumentException Authorization 헤더가 잘못되었거나 유효하지 않은 경우
//     */
//
//    @GetMapping("/validate")
//    public ResponseEntity<ApiResult> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
//        try {
//
//            String token = jwtUtil.getTokenFromHeader(authorizationHeader);
//
//            if (jwtUtil.validToken(token)) {
//                ApiResult result = ApiResult.builder()
//                        .status(StatusEnum.SUCCESS)
//                        .message("유효한 토큰입니다.")
//                        .build();
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                ApiResult result = ApiResult.builder()
//                        .status(StatusEnum.FAILURE)
//                        .message("유효하지 않은 토큰입니다.")
//                        .build();
//                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
//            }
//        } catch (IllegalArgumentException e) {
//            ApiResult result = ApiResult.builder()
//                    .status(StatusEnum.FAILURE)
//                    .message("잘못된 요청입니다: " + e.getMessage())
//                    .build();
//            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//        }
//    }
//
//}