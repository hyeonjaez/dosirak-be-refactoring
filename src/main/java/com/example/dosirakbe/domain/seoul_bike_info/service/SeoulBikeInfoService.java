package com.example.dosirakbe.domain.seoul_bike_info.service;

import com.example.dosirakbe.domain.seoul_bike_info.dto.mapper.SeoulBikeInfoMapper;
import com.example.dosirakbe.domain.seoul_bike_info.dto.response.SeoulBikeInfoResponse;
import com.example.dosirakbe.domain.seoul_bike_info.entity.SeoulBikeInfo;
import com.example.dosirakbe.domain.seoul_bike_info.repository.SeoulBikeInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeoulBikeInfoService {
    private final SeoulBikeInfoRepository seoulBikeInfoRepository;
    private final SeoulBikeInfoMapper seoulBikeInfoMapper;

    public List<SeoulBikeInfoResponse> getSeoulBikeListAroundMe(BigDecimal myLatitude, BigDecimal myLongitude) {
        List<SeoulBikeInfo> allWithinDistance = seoulBikeInfoRepository.findAllWithinDistance(myLatitude, myLongitude);

        return seoulBikeInfoMapper.mapToSeoulBikeInfoResponseList(allWithinDistance);
    }

}
