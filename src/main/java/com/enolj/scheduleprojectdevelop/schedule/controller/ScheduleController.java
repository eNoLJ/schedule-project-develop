package com.enolj.scheduleprojectdevelop.schedule.controller;

import com.enolj.scheduleprojectdevelop.schedule.dto.*;
import com.enolj.scheduleprojectdevelop.schedule.service.ScheduleService;
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
    public ResponseEntity<CreateScheduleResponse> create(@Valid @RequestBody CreateScheduleRequest request) {
        CreateScheduleResponse response = scheduleService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/schedules")
    public ResponseEntity<List<GetSchedulesResponse>> getAll(@RequestParam(required = false) String author) {
        List<GetSchedulesResponse> responseList = scheduleService.findAll(author);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/schedules/{scheduleId}")
    public ResponseEntity<GetScheduleResponse> getOne(@PathVariable Long scheduleId) {
        GetScheduleResponse response = scheduleService.findOne(scheduleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/schedules/{scheduleId}")
    public ResponseEntity<UpdateScheduleResponse> update(@PathVariable Long scheduleId, @Valid @RequestBody UpdateScheduleRequest request) {
        UpdateScheduleResponse response = scheduleService.update(scheduleId, request);
        return ResponseEntity.ok(response);
    }
}
