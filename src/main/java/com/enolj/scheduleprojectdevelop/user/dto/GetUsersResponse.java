package com.enolj.scheduleprojectdevelop.user.dto;

import com.enolj.scheduleprojectdevelop.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetUsersResponse {

    private final Long id;
    private final String name;
    private final String email;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static GetUsersResponse from(User user) {
        return GetUsersResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .modifiedAt(user.getModifiedAt())
                .build();
    }
}
