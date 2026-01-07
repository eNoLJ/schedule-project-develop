package com.enolj.scheduleprojectdevelop.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    private String name;
    @NotBlank
    private String currentPassword;
    @Size(min = 8)
    private String newPassword;
}
