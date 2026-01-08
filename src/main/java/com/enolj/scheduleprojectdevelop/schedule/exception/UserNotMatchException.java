package com.enolj.scheduleprojectdevelop.schedule.exception;

import com.enolj.scheduleprojectdevelop.global.exception.ErrorCode;
import com.enolj.scheduleprojectdevelop.global.exception.ServiceException;

public class UserNotMatchException extends ServiceException {
    public UserNotMatchException(ErrorCode errorCode) {
        super(errorCode.getStatus(), errorCode.getMessage());
    }
}
