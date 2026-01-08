package com.enolj.scheduleprojectdevelop.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateCommentRequest {

    @Size(max = 200, message = "내용은 200자 이하여야 합니다.")
    private String description;
}
