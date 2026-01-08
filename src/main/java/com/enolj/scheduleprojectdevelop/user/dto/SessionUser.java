package com.enolj.scheduleprojectdevelop.user.dto;

import com.enolj.scheduleprojectdevelop.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionUser {

    private final Long id;
    private final String email;

    public static SessionUser from(User user) {
        return SessionUser.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}
