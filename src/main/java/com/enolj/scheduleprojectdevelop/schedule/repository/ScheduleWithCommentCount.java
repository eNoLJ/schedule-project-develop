package com.enolj.scheduleprojectdevelop.schedule.repository;

import com.enolj.scheduleprojectdevelop.schedule.entity.Schedule;

public interface ScheduleWithCommentCount {

    Schedule getSchedule();
    long getCommentCount();
}
