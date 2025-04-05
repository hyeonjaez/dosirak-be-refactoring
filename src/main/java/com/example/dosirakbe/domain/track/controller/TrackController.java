//package com.example.dosirakbe.domain.track.controller;
//
//import com.example.dosirakbe.domain.auth.dto.response.CustomOAuth2User;
//import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
//import com.example.dosirakbe.domain.track.dto.response.TrackMoveResponse;
//import com.example.dosirakbe.domain.track.service.TrackService;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//
///**
// * packageName    : com.example.dosirakbe.domain.track.controller<br>
// * fileName       : TrackController<br>
// * author         : Fiat_lux<br>
// * date           : 11/14/24<br>
// * description    : 트랙 관련 API 요청을 처리하는 컨트롤러 클래스입니다.<br>
// * ===========================================================<br>
// * DATE              AUTHOR             NOTE<br>
// * -----------------------------------------------------------<br>
// * 11/14/24        Fiat_lux                최초 생성<br>
// */
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/tracks")
//public class TrackController {
//    public final TrackService trackService;
//    public static final BigDecimal GAP_DISTANCE = BigDecimal.ONE; //1km
//
//    /**
//     * 사용자의 이동 거리를 기록하고 저장한 후, 해당 정보를 {@link TrackMoveResponse} Response DTO 로 반환합니다.
//     *
//     * <p>
//     * 이 메서드는 인증된 사용자의 ID와 채팅 방 ID를 기반으로 사용자의 이동 거리를 기록합니다.
//     * 이동 거리가 허용된 간격 내에 있는지 검증한 후, 트랙 정보를 저장하고 응답으로 반환합니다.
//     * </p>
//     *
//     * @param customOAuth2User 인증된 사용자의 정보를 포함하는 {@link CustomOAuth2User} 객체
//     * @param trackMoveRequest 사용자의 이동 거리를 포함하는 {@link TrackMoveRequest} 객체
//     * @return 기록된 이동 거리를 포함하는 {@link ApiResult} 형태의 {@link TrackMoveResponse} 객체
//     * @throws ApiException {@link ExceptionEnum#INVALID_REQUEST} 예외 발생 시
//     */
//    @PostMapping
//    public ResponseEntity<ApiResult<TrackMoveResponse>> recordMovingDistance(@AuthenticationPrincipal CustomOAuth2User customOAuth2User,
//                                                                             @RequestBody @Valid TrackMoveRequest trackMoveRequest) {
//        Long userId = getUserId(customOAuth2User);
//
//        if (!checkGapDistance(trackMoveRequest)) {
//            throw new ApiException(ExceptionEnum.INVALID_REQUEST);
//        }
//
//        TrackMoveResponse trackMoveResponse = trackService.recordTrackDistance(userId, trackMoveRequest);
//        ApiResult<TrackMoveResponse> result = ApiResult.<TrackMoveResponse>builder()
//                .status(StatusEnum.SUCCESS)
//                .message("track move distance successful")
//                .data(trackMoveResponse)
//                .build();
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(result);
//    }
//
//    /**
//     * 인증된 사용자의 ID를 추출합니다.
//     *
//     * <p>
//     * 이 메서드는 {@link CustomOAuth2User} 객체에서 사용자의 고유 ID를 추출하여 반환합니다.
//     * </p>
//     *
//     * @param customOAuth2User 인증된 사용자의 정보를 포함하는 {@link CustomOAuth2User} 객체
//     * @return 사용자의 고유 식별자 {@link Long} 값
//     */
//    private Long getUserId(CustomOAuth2User customOAuth2User) {
//        return customOAuth2User.getUserDTO().getUserId();
//    }
//
//    /**
//     * 이동 거리가 허용된 간격 내에 있는지 검증합니다.
//     *
//     * <p>
//     * 이 메서드는 요청된 이동 거리와 최단 이동 거리 간의 차이가 {@link #GAP_DISTANCE} 이내인지 확인합니다.
//     * </p>
//     *
//     * @param trackMoveRequest 사용자의 이동 거리를 포함하는 {@link TrackMoveRequest} 객체
//     * @return 이동 거리가 허용된 간격 내에 있으면 {@code true}, 그렇지 않으면 {@code false}
//     */
//    private boolean checkGapDistance(TrackMoveRequest trackMoveRequest) {
//        return trackMoveRequest.getMoveDistance()
//                .subtract(trackMoveRequest.getShortestDistance())
//                .abs()
//                .compareTo(GAP_DISTANCE) <= 0;
//    }
//}
