//package com.example.dosirakbe.domain.track.service;
//
//import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
//import com.example.dosirakbe.domain.green_commit.event.GreenCommitEvent;
//import com.example.dosirakbe.domain.salestore.entity.SaleStore;
//import com.example.dosirakbe.domain.salestore.repository.SaleStoreRepository;
//import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
//import com.example.dosirakbe.domain.track.dto.response.TrackMoveResponse;
//import com.example.dosirakbe.domain.track.entity.Track;
//import com.example.dosirakbe.domain.track.repository.TrackRepository;
//import com.example.dosirakbe.domain.user.entity.User;
//import com.example.dosirakbe.domain.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * packageName    : com.example.dosirakbe.domain.track.service<br>
// * fileName       : TrackService<br>
// * author         : Fiat_lux<br>
// * date           : 11/14/24<br>
// * description    : 트랙 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.<br>
// * ===========================================================<br>
// * DATE              AUTHOR             NOTE<br>
// * -----------------------------------------------------------<br>
// * 11/14/24        Fiat_lux                최초 생성<br>
// */
//@RequiredArgsConstructor
//@Service
//@Transactional
//public class TrackService {
//    private final UserRepository userRepository;
//    private final ApplicationEventPublisher eventPublisher;
//    private final SaleStoreRepository saleStoreRepository;
//    private final TrackRepository trackRepository;
//
//
//    /**
//     * 사용자의 이동 거리를 기록하고 저장한 후, 해당 정보를 {@link TrackMoveResponse} DTO로 반환합니다.
//     *
//     * <p>
//     * 이 메서드는 사용자의 ID와 이동 요청 정보를 기반으로 트랙을 생성하고 저장합니다.
//     * 이동 거리와 관련된 비즈니스 로직을 처리하며, 관련 이벤트를 발행합니다.
//     * </p>
//     *
//     * @param userId           메시지를 기록하는 사용자의 고유 식별자 {@link Long}
//     * @param trackMoveRequest 사용자의 이동 거리 기록을 포함하는 {@link TrackMoveRequest} 객체
//     * @return 기록된 이동 거리를 포함하는 {@link TrackMoveResponse} DTO 객체
//     * @throws ApiException {@link com.example.dosirakbe.global.util.ApiException} 발생 시
//     */
//    public TrackMoveResponse recordTrackDistance(Long userId, TrackMoveRequest trackMoveRequest) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(
//                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND)
//                );
//
//        SaleStore saleStore = saleStoreRepository.findBySaleStoreName(trackMoveRequest.getSaleStoreName())
//                .orElseThrow(
//                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND)
//                );
//
//        Track track = new Track(saleStore.getSaleStoreName(), saleStore.getSaleStoreAddress(), trackMoveRequest.getMoveDistance(), user);
//
//        trackRepository.save(track);
//
//        user.addTrackDistance(trackMoveRequest.getMoveDistance());
//        eventPublisher.publishEvent(new GreenCommitEvent(this, user.getUserId(), null, ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION, trackMoveRequest.getMoveDistance()));
//
//        return new TrackMoveResponse(trackMoveRequest.getMoveDistance());
//    }
//}