package com.example.dosirakbe.domain.activity_log.dto.mapper;

import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ActivityLogMapper {

    @Mapping(source = "createdAt", target = "createAtTime", qualifiedByName = "formatDateTime")
    @Mapping(target = "activityMessage", source = "activityLog", qualifiedByName = "generateActivityMessage")
    @Mapping(source = "activityType.iconImageUrl", target = "iconImageUrl")
    ActivityLogResponse mapToActivityLogResponse(ActivityLog activityLog);

    List<ActivityLogResponse> mapToActivityLogResponseList(List<ActivityLog> activityLogs);

    @Named("formatDateTime")
    default String formatDateTime(LocalDateTime createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분");
        return createdAt.format(formatter);
    }

    @Named("generateActivityMessage")
    default String generateActivityMessage(ActivityLog activityLog) {
        return activityLog.getActivityType().generateMessage(activityLog.getDistance());
    }
}
