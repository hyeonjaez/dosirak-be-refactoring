package com.example.dosirakbe.domain.seoul_bike_info.service;

import com.example.dosirakbe.domain.seoul_bike_info.dto.mapper.SeoulBikeInfoMapper;
import com.example.dosirakbe.domain.seoul_bike_info.dto.request.SeoulBikeInfoRequest;
import com.example.dosirakbe.domain.seoul_bike_info.dto.response.SeoulBikeInfoResponse;
import com.example.dosirakbe.domain.seoul_bike_info.entity.SeoulBikeInfo;
import com.example.dosirakbe.domain.seoul_bike_info.repository.SeoulBikeInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeoulBikeInfoService {
    private final SeoulBikeInfoRepository seoulBikeInfoRepository;
    private final SeoulBikeInfoMapper seoulBikeInfoMapper;

    public List<SeoulBikeInfoResponse> getSeoulBikeListAroundMe(SeoulBikeInfoRequest seoulBikeInfoRequest) {
        List<SeoulBikeInfo> allWithinDistance = seoulBikeInfoRepository.findAllWithinDistance(seoulBikeInfoRequest.getMyLatitude(), seoulBikeInfoRequest.getMyLongitude());

        return seoulBikeInfoMapper.mapToSeoulBikeInfoResponseList(allWithinDistance);
    }

}
