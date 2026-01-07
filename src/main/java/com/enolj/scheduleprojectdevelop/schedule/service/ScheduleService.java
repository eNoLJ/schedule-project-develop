package com.enolj.scheduleprojectdevelop.schedule.service;

import com.enolj.scheduleprojectdevelop.schedule.dto.CreateScheduleRequest;
import com.enolj.scheduleprojectdevelop.schedule.dto.CreateScheduleResponse;
import com.enolj.scheduleprojectdevelop.schedule.dto.GetSchedulesResponse;
import com.enolj.scheduleprojectdevelop.schedule.entity.Schedule;
import com.enolj.scheduleprojectdevelop.schedule.repository.ScheduleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    @Transactional
    public CreateScheduleResponse save(@Valid CreateScheduleRequest request) {
        Schedule schedule = Schedule.from(request);
        scheduleRepository.save(schedule);
        return CreateScheduleResponse.from(schedule);
    }

    @Transactional(readOnly = true)
    public List<GetSchedulesResponse> findAll(String author) {
        List<Schedule> schedules = (author == null)
                ? scheduleRepository.findAllByOrderByModifiedAtDesc()
                : scheduleRepository.findAllByAuthorOrderByModifiedAtDesc(author);

        return schedules.stream()
                .map(GetSchedulesResponse::from)
                .toList();
    }
}
