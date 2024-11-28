package com.example.dosirakbe.domain.track.repository;

import com.example.dosirakbe.domain.track.entity.Track;
import com.example.dosirakbe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findByUserAndStoreNameContainingIgnoreCaseOrderByCreatedAtDesc(User user, String storeName);

}
