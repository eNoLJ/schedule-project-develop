package com.enolj.scheduleprojectdevelop.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 스케줄을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복 된 이메일입니다.");

    private final HttpStatus status;
    private final String message;
}
