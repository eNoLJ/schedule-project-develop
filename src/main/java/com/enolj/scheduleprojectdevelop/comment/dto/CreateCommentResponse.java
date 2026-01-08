package com.enolj.scheduleprojectdevelop.comment.dto;

import com.enolj.scheduleprojectdevelop.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CreateCommentResponse {

    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CreateCommentResponse from(Comment comment) {
        return CreateCommentResponse.builder()
                .description(comment.getDescription())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
