package com.example.dosirakbe.domain.track.dto.mapper;

import com.example.dosirakbe.domain.track.dto.response.TrackSearchResponse;
import com.example.dosirakbe.domain.track.entity.Track;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TrackMapper {

    TrackSearchResponse mapToTrackSearchResponse(Track track);

    List<TrackSearchResponse> mapToTrackSearchResponseList(List<Track> tracks);
}
