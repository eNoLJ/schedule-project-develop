package com.enolj.scheduleprojectdevelop.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateScheduleRequest {

    @NotBlank(message = "제목을 입력해 주세요.")
    @Size(max = 30, message = "제목은 30자 이하여야 합니다.")
    private String title;
    @NotBlank(message = "내용을 입력해 주세요.")
    @Size(max = 200, message = "내용은 200자 이하여야 합니다.")
    private String description;
    @NotBlank(message = "작성자를 입력해 주세요.")
    private String author;
}
