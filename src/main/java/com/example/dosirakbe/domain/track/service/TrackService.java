package com.example.dosirakbe.domain.track.service;

import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import com.example.dosirakbe.domain.green_commit.event.GreenCommitEvent;
import com.example.dosirakbe.domain.salestore.entity.SaleStore;
import com.example.dosirakbe.domain.salestore.repository.SaleStoreRepository;
import com.example.dosirakbe.domain.track.dto.mapper.TrackMapper;
import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
import com.example.dosirakbe.domain.track.dto.response.TrackMoveResponse;
import com.example.dosirakbe.domain.track.dto.response.TrackSearchResponse;
import com.example.dosirakbe.domain.track.entity.Track;
import com.example.dosirakbe.domain.track.repository.TrackRepository;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional
public class TrackService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SaleStoreRepository saleStoreRepository;
    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    public TrackMoveResponse recordTrackDistance(Long userId, TrackMoveRequest trackMoveRequest) {
        User user = userRepository.findById(userId).orElseThrow();

        SaleStore saleStore = saleStoreRepository.findBySaleStoreName(trackMoveRequest.getSaleStoreName()).orElseThrow();

        Track track = new Track(saleStore.getSaleStoreName(), saleStore.getSaleStoreAddress(), trackMoveRequest.getMoveDistance(), user);

        Track saveTrack = trackRepository.save(track);

        user.addTrackDistance(trackMoveRequest.getMoveDistance());
        eventPublisher.publishEvent(new GreenCommitEvent(this, user.getUserId(), null, ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION, trackMoveRequest.getMoveDistance()));

        return new TrackMoveResponse(trackMoveRequest.getMoveDistance());
    }

    @Transactional(readOnly = true)
    public List<TrackSearchResponse> getTrackList(Long userId, String search) {
        User user = userRepository.findById(userId).orElseThrow();

        List<Track> trackList = trackRepository.findByUserAndStoreNameContainingIgnoreCaseOrderByCreatedAtDesc(user, search);

        return trackMapper.mapToTrackSearchResponseList(trackList);
    }
}