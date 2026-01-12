package com.enolj.scheduleprojectdevelop.schedule.controller;

import com.enolj.scheduleprojectdevelop.schedule.dto.*;
import com.enolj.scheduleprojectdevelop.schedule.service.ScheduleService;
import com.enolj.scheduleprojectdevelop.user.dto.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성 API
    // 로그인한 사용자를 작성자로 사용
    // 요청 바디에 전달된 일정 정보를 검증 후 저장
    // 일정 생성 성공 시 201 created 반환
    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> create(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @Valid @RequestBody CreateScheduleRequest request
    ) {
        CreateScheduleResponse response = scheduleService.save(sessionUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 일정 목록 조회 API
    // author 파라미터가 있으면 해당 작성자의 일정만 조회
    // page, size를 이용한 페이징 처리
    // page는 클라이언트 기준이므로 내부에서 -1 처리
    @GetMapping("/schedules")
    public ResponseEntity<List<GetSchedulesResponse>> getAll(
            @RequestParam(required = false) String author,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<GetSchedulesResponse> responseList = scheduleService.findAll(author, page - 1, size);
        return ResponseEntity.ok(responseList);
    }

    // 일정 단건 조회
    // 일정 ID를 PathVariable로 전달받아 조회
    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getOne(@PathVariable Long scheduleId) {
        GetScheduleResponse response = scheduleService.findOne(scheduleId);
        return ResponseEntity.ok(response);
    }

    // 일정 수정 API
    // 로그인한 사용자만 수정 가능
    // 작성자 검증은 서비스 계층에서 수행
    // 요청 값은 @Valid를 통해 검증
    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> update(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId,
            @Valid @RequestBody UpdateScheduleRequest request
    ) {
        UpdateScheduleResponse response = scheduleService.update(sessionUser, scheduleId, request);
        return ResponseEntity.ok(response);
    }

    // 일정 삭제 API
    // 로그인한 사용자만 삭제 가능
    // 작성자 검증은 서비스 계층에서 수행
    // 정상적으로 삭제 시 204 No Content 반환
    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> delete(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId
    ) {
        scheduleService.delete(sessionUser, scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
