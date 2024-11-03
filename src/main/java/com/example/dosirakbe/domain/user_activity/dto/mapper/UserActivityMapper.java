package com.example.dosirakbe.domain.user_activity.dto.mapper;

import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserActivityMapper {
    UserActivityResponse mapToUserActivityResponse(UserActivity userActivity);

    List<UserActivityResponse> mapToUserActivityResponseList(List<UserActivity> userActivityList);
}
