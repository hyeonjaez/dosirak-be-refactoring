package com.example.dosirakbe.domain.activity_log.dto.mapper;

import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ActivityLogMapper {

    @Mapping(source = "activityType.message", target = "activityMessage")
    @Mapping(source = "activityType.iconImageUrl", target = "iconImageUrl")
    @Mapping(source = "createdAt", target = "createAtTime", qualifiedByName = "formatDateTime")
    ActivityLogResponse mapToActivityLogResponse(ActivityLog activityLog);

    List<ActivityLogResponse> mapToActivityLogResponseList(List<ActivityLog> activityLogs);

    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분");
        return createdAt.format(formatter);
    }
}
