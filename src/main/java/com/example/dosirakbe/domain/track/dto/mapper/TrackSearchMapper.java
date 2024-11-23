package com.example.dosirakbe.domain.track.dto.mapper;

import com.example.dosirakbe.domain.track.dto.response.TrackSearchGetResponse;
import com.example.dosirakbe.domain.track.entity.TrackSearch;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TrackSearchMapper {

    TrackSearchGetResponse mapToResponse(TrackSearch trackSearch);

    List<TrackSearchGetResponse> mapToResponseList(List<TrackSearch> trackSearchList);
}
