package com.example.dosirakbe.domain.track.repository;

import com.example.dosirakbe.domain.track.entity.TrackSearch;
import com.example.dosirakbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackSearchRepository extends JpaRepository<TrackSearch, Long> {
    List<TrackSearch> findAllByUserOrderByCreatedAtDesc(User user);
}
