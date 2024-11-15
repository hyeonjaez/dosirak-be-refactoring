package com.example.dosirakbe.domain.seoul_bike_info.dto.mapper;

import com.example.dosirakbe.domain.seoul_bike_info.dto.response.SeoulBikeInfoResponse;
import com.example.dosirakbe.domain.seoul_bike_info.entity.SeoulBikeInfo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface SeoulBikeInfoMapper {

    SeoulBikeInfoResponse mapToSeoulBikeInfoResponse(SeoulBikeInfo seoulBikeInfo);

    List<SeoulBikeInfoResponse> mapToSeoulBikeInfoResponseList(List<SeoulBikeInfo> seoulBikeInfoList);
}
