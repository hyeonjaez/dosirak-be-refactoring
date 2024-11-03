package com.example.dosirakbe.domain.activity_log.dto.mapper;

import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ActivityLogMapper {
    @Mapping(source = "activityType.type.message", target = "activityMessage")
    ActivityLogResponse mapToActivityLogResponse(ActivityLog activityLog);

    List<ActivityLogResponse> mapToActivityLogResponseList(List<ActivityLog> activityLogs);
}
