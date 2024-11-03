package com.example.dosirakbe.domain.activity_log.dto.mapper;

import com.example.dosirakbe.domain.activity_log.dto.response.ActivityLogResponse;
import com.example.dosirakbe.domain.activity_log.entity.ActivityLog;
import com.example.dosirakbe.domain.activity_type.entity.ActivityType;
import com.example.dosirakbe.domain.activity_type.entity.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-03T21:12:24+0900",
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
        LocalDateTime createdAt = null;

        activityMessage = activityLogActivityTypeTypeMessage( activityLog );
        createdAt = activityLog.getCreatedAt();

        ActivityLogResponse activityLogResponse = new ActivityLogResponse( createdAt, activityMessage );

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

    private String activityLogActivityTypeTypeMessage(ActivityLog activityLog) {
        if ( activityLog == null ) {
            return null;
        }
        ActivityType activityType = activityLog.getActivityType();
        if ( activityType == null ) {
            return null;
        }
        Type type = activityType.getType();
        if ( type == null ) {
            return null;
        }
        String message = type.message;
        if ( message == null ) {
            return null;
        }
        return message;
    }
}
