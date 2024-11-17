package com.example.dosirakbe.domain.track.service;

import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import com.example.dosirakbe.domain.green_commit.event.GreenCommitEvent;
import com.example.dosirakbe.domain.track.dto.request.TrackMoveRequest;
import com.example.dosirakbe.domain.track.dto.response.TrackMoveResponse;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional
public class TrackService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public TrackMoveResponse recordTrackDistance(Long userId, TrackMoveRequest trackMoveRequest) {
        User user = userRepository.findById(userId).orElseThrow();

        user.addTrackDistance(trackMoveRequest.getMoveDistance());
        eventPublisher.publishEvent(new GreenCommitEvent(this, user.getUserId(), null, ActivityType.LOW_CARBON_MEANS_OF_TRANSPORTATION, trackMoveRequest.getMoveDistance()));

        return new TrackMoveResponse(trackMoveRequest.getMoveDistance());
    }
}
