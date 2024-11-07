package com.example.dosirakbe.domain.user_activity.dto.mapper;

import com.example.dosirakbe.domain.user_activity.dto.response.UserActivityResponse;
import com.example.dosirakbe.domain.user_activity.entity.UserActivity;
import java.time.LocalDate;
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
public class UserActivityMapperImpl implements UserActivityMapper {

    @Override
    public UserActivityResponse mapToUserActivityResponse(UserActivity userActivity) {
        if ( userActivity == null ) {
            return null;
        }

        LocalDate createdAt = null;
        Integer commitCount = null;

        createdAt = userActivity.getCreatedAt();
        commitCount = userActivity.getCommitCount();

        UserActivityResponse userActivityResponse = new UserActivityResponse( createdAt, commitCount );

        return userActivityResponse;
    }

    @Override
    public List<UserActivityResponse> mapToUserActivityResponseList(List<UserActivity> userActivityList) {
        if ( userActivityList == null ) {
            return null;
        }

        List<UserActivityResponse> list = new ArrayList<UserActivityResponse>( userActivityList.size() );
        for ( UserActivity userActivity : userActivityList ) {
            list.add( mapToUserActivityResponse( userActivity ) );
        }

        return list;
    }
}
