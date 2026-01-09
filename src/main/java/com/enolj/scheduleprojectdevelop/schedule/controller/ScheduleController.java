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

    @PostMapping("/schedules")
    public ResponseEntity<CreateScheduleResponse> create(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @Valid @RequestBody CreateScheduleRequest request
    ) {
        CreateScheduleResponse response = scheduleService.save(sessionUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<GetSchedulesResponse>> getAll(
            @RequestParam(required = false) String author,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<GetSchedulesResponse> responseList = scheduleService.findAll(author, page - 1, size);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getOne(@PathVariable Long scheduleId) {
        GetScheduleResponse response = scheduleService.findOne(scheduleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> update(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId,
            @Valid @RequestBody UpdateScheduleRequest request
    ) {
        UpdateScheduleResponse response = scheduleService.update(sessionUser, scheduleId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/schedules/{scheduleId}")
    public ResponseEntity<Void> delete(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @PathVariable Long scheduleId
    ) {
        scheduleService.delete(sessionUser, scheduleId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
