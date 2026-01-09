package com.enolj.scheduleprojectdevelop.schedule.dto;

import com.enolj.scheduleprojectdevelop.schedule.entity.Schedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetSchedulesResponse {

    private final Long id;
    private final String title;
    private final String author;
    private final int commentCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static GetSchedulesResponse from(Schedule schedule, int commentCount) {
        return GetSchedulesResponse.builder()
                .id(schedule.getId())
                .title(schedule.getTitle())
                .author(schedule.getUserName())
                .commentCount(commentCount)
                .createdAt(schedule.getCreatedAt())
                .modifiedAt(schedule.getModifiedAt())
                .build();
    }
}
