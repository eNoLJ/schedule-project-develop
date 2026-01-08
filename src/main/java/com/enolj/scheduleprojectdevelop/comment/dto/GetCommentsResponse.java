package com.enolj.scheduleprojectdevelop.comment.dto;

import com.enolj.scheduleprojectdevelop.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class GetCommentsResponse {

    private final String description;
    private final String author;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static GetCommentsResponse from(Comment comment) {
        return GetCommentsResponse.builder()
                .description(comment.getDescription())
                .author(comment.getUserName())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}
