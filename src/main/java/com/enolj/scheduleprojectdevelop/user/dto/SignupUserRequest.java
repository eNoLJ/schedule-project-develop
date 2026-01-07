package com.enolj.scheduleprojectdevelop.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupUserRequest {

    @NotBlank(message = "이름을 입력해 주세요.")
    private String name;
    @Email(message = "올바른 이메일 형식을 입력해 주세요")
    private String email;
    @Size(min = 8, message = "비밀번호는 8글자 이상이어야 합니다.")
    private String password;
}
