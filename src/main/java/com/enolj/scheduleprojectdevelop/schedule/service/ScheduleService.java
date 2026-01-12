package com.enolj.scheduleprojectdevelop.schedule.service;

import com.enolj.scheduleprojectdevelop.global.exception.ErrorCode;
import com.enolj.scheduleprojectdevelop.schedule.dto.*;
import com.enolj.scheduleprojectdevelop.schedule.entity.Schedule;
import com.enolj.scheduleprojectdevelop.schedule.exception.ScheduleNotFoundException;
import com.enolj.scheduleprojectdevelop.schedule.exception.UserNotMatchException;
import com.enolj.scheduleprojectdevelop.schedule.repository.ScheduleRepository;
import com.enolj.scheduleprojectdevelop.schedule.repository.ScheduleWithCommentCount;
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
    private final UserService userService;

    // 일정 생성
    // 세션 사용자 정보를 기반으로 작성자를 설정
    // 새로운 일정을 저장하고 응답 DTO로 반환
    @Transactional
    public CreateScheduleResponse save(SessionUser sessionUser, CreateScheduleRequest request) {
        User user = userService.findById(sessionUser.getId());
        Schedule schedule = Schedule.from(user, request);
        scheduleRepository.save(schedule);
        return CreateScheduleResponse.from(schedule);
    }

    // 일정 목록 조회(페이징)
    // 수정일 기준 내림차순 정렬
    // 작성자가 있으면 해당 작성자의 일정만 조회
    // 각 일정마다 댓글 개수를 함께 조회하여 응답에 포함
    @Transactional(readOnly = true)
    public List<GetSchedulesResponse> findAll(String author, int page, int size) {
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "modifiedAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ScheduleWithCommentCount> schedules = scheduleRepository.findAllWithCommentCount(author, pageable);

        return schedules.stream()
                .map(scheduleWithCommentCount ->
                        GetSchedulesResponse.from(scheduleWithCommentCount.getSchedule(), (int) scheduleWithCommentCount.getCommentCount()))
                .toList();
    }

    // 일정 단건 조회
    // 일정 ID를 기준으로 일정 정보 조회
    @Transactional(readOnly = true)
    public GetScheduleResponse findOne(Long scheduleId) {
        Schedule schedule = findById(scheduleId);
        return GetScheduleResponse.from(schedule);
    }

    // 일정 수정
    // 로그인한 사용자와 일정 작성자가 일치하는지 검증
    // 일정을 업데이트 하고 응답 DTO로 반환
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

    // 일정 삭제
    // 로그인한 사용자와 일정 작성자가 일치하는지 검증 후 삭제
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
