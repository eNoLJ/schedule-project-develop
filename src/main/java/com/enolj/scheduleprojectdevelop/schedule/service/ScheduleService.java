package com.enolj.scheduleprojectdevelop.schedule.service;

import com.enolj.scheduleprojectdevelop.comment.repository.CommentRepository;
import com.enolj.scheduleprojectdevelop.global.exception.ErrorCode;
import com.enolj.scheduleprojectdevelop.schedule.dto.*;
import com.enolj.scheduleprojectdevelop.schedule.entity.Schedule;
import com.enolj.scheduleprojectdevelop.schedule.exception.ScheduleNotFoundException;
import com.enolj.scheduleprojectdevelop.schedule.exception.UserNotMatchException;
import com.enolj.scheduleprojectdevelop.schedule.repository.ScheduleRepository;
import com.enolj.scheduleprojectdevelop.user.dto.SessionUser;
import com.enolj.scheduleprojectdevelop.user.entity.User;
import com.enolj.scheduleprojectdevelop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    @Transactional
    public CreateScheduleResponse save(SessionUser sessionUser, CreateScheduleRequest request) {
        User user = userService.findById(sessionUser.getId());
        Schedule schedule = Schedule.from(user, request);
        scheduleRepository.save(schedule);
        return CreateScheduleResponse.from(schedule);
    }

    @Transactional(readOnly = true)
    public List<GetSchedulesResponse> findAll(String author, int page, int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "modifiedAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Schedule> schedules = (author == null)
                ? scheduleRepository.findAll(pageable)
                : scheduleRepository.findAllByUser_Name(author, pageable);

        return schedules.stream()
                .map(schedule -> {
                    int commentCount = commentRepository.countByScheduleId(schedule.getId());
                    return GetSchedulesResponse.from(schedule, commentCount);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public GetScheduleResponse findOne(Long scheduleId) {
        Schedule schedule = findById(scheduleId);
        return GetScheduleResponse.from(schedule);
    }


    @Transactional
    public UpdateScheduleResponse update(SessionUser sessionUser, Long scheduleId, UpdateScheduleRequest request) {
        User user = userService.findById(sessionUser.getId());
        Schedule schedule = findById(scheduleId);
        validateScheduleUser(schedule, user);
        schedule.update(request);

        // 수정일 반영을 하기 위한 flush()
        scheduleRepository.flush();
        return UpdateScheduleResponse.from(schedule);
    }

    @Transactional
    public void delete(SessionUser sessionUser, Long scheduleId) {
        User user = userService.findById(sessionUser.getId());
        Schedule schedule = findById(scheduleId);
        validateScheduleUser(schedule, user);
        scheduleRepository.delete(schedule);
    }

    public Schedule findById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new ScheduleNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND)
        );
    }

    private void validateScheduleUser(Schedule schedule, User user) {
        if (!schedule.matchesUser(user)) {
            throw new UserNotMatchException(ErrorCode.USER_NOT_MATCH);
        }
    }
}
