package com.example.dosirakbe.domain.track.dto.mapper;

import com.example.dosirakbe.domain.track.dto.response.TrackSearchResponse;
import com.example.dosirakbe.domain.track.entity.Track;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * packageName    : com.example.dosirakbe.domain.track.dto.mapper<br>
 * fileName       : TrackMapper<br>
 * author         : Fiat_lux<br>
 * date           : 11/24/24<br>
 * description    : 트랙 엔티티와 DTO 간의 매핑을 담당하는 매퍼 인터페이스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/24/24        Fiat_lux                최초 생성<br>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TrackMapper {

    /**
     * {@link Track} 엔티티를 {@link TrackSearchResponse} Response DTO 로 변환합니다.
     *
     * <p>
     * 이 메서드는 {@link Track} 엔티티의 데이터를 {@link TrackSearchResponse} Response DTO 로 매핑하여 반환합니다.
     * </p>
     *
     * @param track 매핑할 {@link Track} 엔티티 객체
     * @return 매핑된 {@link TrackSearchResponse} DTO 객체
     */
    TrackSearchResponse mapToTrackSearchResponse(Track track);

    /**
     * {@link Track} 엔티티 리스트를 {@link TrackSearchResponse} DTO 리스트로 변환합니다.
     *
     * <p>
     * 이 메서드는 {@link Track} 엔티티의 리스트를 {@link TrackSearchResponse} Response DTO 의 리스트로 매핑하여 반환합니다.
     * </p>
     *
     * @param tracks 매핑할 {@link Track} 엔티티 객체의 리스트
     * @return 매핑된 {@link TrackSearchResponse} Response DTO 객체의 리스트
     */
    List<TrackSearchResponse> mapToTrackSearchResponseList(List<Track> tracks);
}
