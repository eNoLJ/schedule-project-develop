package com.enolj.scheduleprojectdevelop.user.exception;

import com.enolj.scheduleprojectdevelop.global.exception.ErrorCode;
import com.enolj.scheduleprojectdevelop.global.exception.ServiceException;

public class DuplicateEmailException extends ServiceException {
    public DuplicateEmailException(ErrorCode errorCode) {
        super(errorCode.getStatus(), errorCode.getMessage());
    }
}
