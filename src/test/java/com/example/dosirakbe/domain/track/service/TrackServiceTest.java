//package com.example.dosirakbe.domain.track.service;
//
//import com.example.dosirakbe.domain.green_commit.event.GreenCommitEvent;
//import com.example.dosirakbe.domain.salestore.entity.SaleStore;
//import com.example.dosirakbe.domain.salestore.repository.SaleStoreRepository;
//import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
//import com.example.dosirakbe.domain.track.dto.response.TrackMoveResponse;
//import com.example.dosirakbe.domain.track.entity.Track;
//import com.example.dosirakbe.domain.track.repository.TrackRepository;
//import com.example.dosirakbe.domain.user.entity.User;
//import com.example.dosirakbe.domain.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.math.BigDecimal;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class TrackServiceTest {
//
//    @InjectMocks
//    private TrackService trackService;
//
//    @Mock
//    private TrackRepository trackRepository;
//
//    @Mock
//    private SaleStoreRepository saleStoreRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private ApplicationEventPublisher eventPublisher;
//
//    private User user;
//    private SaleStore saleStore;
//    private TrackMoveRequest trackMoveRequest;
//
//
//    @BeforeEach
//    void setUp() {
//        user = mock(User.class);
//        saleStore = mock(SaleStore.class);
//        trackMoveRequest = new TrackMoveRequest();
//        ReflectionTestUtils.setField(trackMoveRequest, "shortestDistance", new BigDecimal("1.0"));
//        ReflectionTestUtils.setField(trackMoveRequest, "moveDistance", new BigDecimal("1.2"));
//        ReflectionTestUtils.setField(trackMoveRequest, "saleStoreName", "Test Store");
//    }
//
//
//    @DisplayName("사용자의 이동 거리를 정상적으로 기록하고 응답을 반환한다.")
//    @Test
//    void recordTrackDistanceTest() {
//        Long userId = 1L;
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(saleStoreRepository.findBySaleStoreName(trackMoveRequest.getSaleStoreName())).thenReturn(Optional.of(saleStore));
//        when(trackRepository.save(any(Track.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        TrackMoveResponse trackMoveResponse = new TrackMoveResponse(trackMoveRequest.getMoveDistance());
//
//        TrackMoveResponse response = trackService.recordTrackDistance(userId, trackMoveRequest);
//
//        assertNotNull(response);
//        assertEquals(trackMoveResponse.getMoveTrackDistance(), response.getMoveTrackDistance());
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(saleStoreRepository, times(1)).findBySaleStoreName(trackMoveRequest.getSaleStoreName());
//        verify(trackRepository, times(1)).save(any(Track.class));
//        verify(user, times(1)).addTrackDistance(trackMoveRequest.getMoveDistance());
//        verify(eventPublisher, times(1)).publishEvent(any(GreenCommitEvent.class));
//    }
//
//    @DisplayName("존재하지 않는 사용자의 경우, ApiException이 발생한다.")
//    @Test
//    void recordTrackDistance_UserNotFound_ThrowsException() {
//        Long userId = 1L;
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        ApiException apiException = assertThrows(ApiException.class,
//                () -> trackService.recordTrackDistance(userId, trackMoveRequest));
//
//        assertEquals(ExceptionEnum.DATA_NOT_FOUND, apiException.getError());
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(saleStoreRepository, times(0)).findBySaleStoreName(trackMoveRequest.getSaleStoreName());
//        verify(trackRepository, times(0)).save(any(Track.class));
//        verify(user, times(0)).addTrackDistance(trackMoveRequest.getMoveDistance());
//        verify(eventPublisher, times(0)).publishEvent(any(GreenCommitEvent.class));
//    }
//
//    @DisplayName("존재하지 않는 판매점의 경우, ApiException이 발생한다.")
//    @Test
//    void recordTrackDistance_SaleStoreNotFound_ThrowsException() {
//        Long userId = 1L;
//        String saleStoreName = trackMoveRequest.getSaleStoreName();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(saleStoreRepository.findBySaleStoreName(saleStoreName)).thenReturn(Optional.empty());
//
//        ApiException exception = assertThrows(ApiException.class, () -> {
//            trackService.recordTrackDistance(userId, trackMoveRequest);
//        });
//
//        assertEquals(ExceptionEnum.DATA_NOT_FOUND, exception.getError());
//
//        verify(userRepository, times(1)).findById(userId);
//        verify(saleStoreRepository, times(1)).findBySaleStoreName(trackMoveRequest.getSaleStoreName());
//        verify(trackRepository, times(0)).save(any(Track.class));
//        verify(user, times(0)).addTrackDistance(trackMoveRequest.getMoveDistance());
//        verify(eventPublisher, times(0)).publishEvent(any(GreenCommitEvent.class));
//    }
//
//
//}