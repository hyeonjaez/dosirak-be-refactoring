package com.example.dosirakbe.domain.track.service;

import com.example.dosirakbe.domain.track.dto.mapper.TrackSearchMapper;
import com.example.dosirakbe.domain.track.dto.response.TrackSearchGetResponse;
import com.example.dosirakbe.domain.track.entity.TrackSearch;
import com.example.dosirakbe.domain.track.repository.TrackSearchRepository;
import com.example.dosirakbe.domain.user.entity.User;
import com.example.dosirakbe.domain.user.repository.UserRepository;
import com.example.dosirakbe.global.util.ApiException;
import com.example.dosirakbe.global.util.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackSearchService {
    private final TrackSearchRepository trackSearchRepository;
    private final UserRepository userRepository;
    private final TrackSearchMapper trackSearchMapper;

    public List<TrackSearchGetResponse> getSearchList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new ApiException(ExceptionEnum.DATA_NOT_FOUND)
                );

        List<TrackSearch> trackList = trackSearchRepository.findAllByUserOrderByCreatedAtDesc(user);

        return trackSearchMapper.mapToResponseList(trackList);
    }


    @Transactional
    public void deleteSearch(Long trackId) {
        if (trackSearchRepository.existsById(trackId)) {
            trackSearchRepository.deleteById(trackId);
            return;
        }

        throw new ApiException(ExceptionEnum.DATA_NOT_FOUND);
    }
}
