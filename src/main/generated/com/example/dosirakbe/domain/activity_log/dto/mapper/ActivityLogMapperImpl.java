package com.example.dosirakbe.domain.activity_log.dto.mapper;

import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import com.example.dosirakbe.domain.activity_log.entity.ActivityType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-06T03:52:36+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ActivityLogMapperImpl implements ActivityLogMapper {

    @Override
    public ActivityLogResponse mapToActivityLogResponse(ActivityLog activityLog) {
        if ( activityLog == null ) {
            return null;
        }

        String activityMessage = null;
        String iconImageUrl = null;
        String createAtTime = null;
        LocalDateTime createdAt = null;

        activityMessage = activityLogActivityTypeMessage( activityLog );
        iconImageUrl = activityLogActivityTypeIconImageUrl( activityLog );
        createAtTime = formatDateTime( activityLog.getCreatedAt() );
        createdAt = activityLog.getCreatedAt();

        ActivityLogResponse activityLogResponse = new ActivityLogResponse( createdAt, activityMessage, createAtTime, iconImageUrl );

        return activityLogResponse;
    }

    @Override
    public List<ActivityLogResponse> mapToActivityLogResponseList(List<ActivityLog> activityLogs) {
        if ( activityLogs == null ) {
            return null;
        }

        List<ActivityLogResponse> list = new ArrayList<ActivityLogResponse>( activityLogs.size() );
        for ( ActivityLog activityLog : activityLogs ) {
            list.add( mapToActivityLogResponse( activityLog ) );
        }

        return list;
    }

    private String activityLogActivityTypeMessage(ActivityLog activityLog) {
        if ( activityLog == null ) {
            return null;
        }
        ActivityType activityType = activityLog.getActivityType();
        if ( activityType == null ) {
            return null;
        }
        String message = activityType.message;
        if ( message == null ) {
            return null;
        }
        return message;
    }

    private String activityLogActivityTypeIconImageUrl(ActivityLog activityLog) {
        if ( activityLog == null ) {
            return null;
        }
        ActivityType activityType = activityLog.getActivityType();
        if ( activityType == null ) {
            return null;
        }
        String iconImageUrl = activityType.iconImageUrl;
        if ( iconImageUrl == null ) {
            return null;
        }
        return iconImageUrl;
    }
}
