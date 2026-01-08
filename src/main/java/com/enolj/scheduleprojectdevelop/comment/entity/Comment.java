package com.enolj.scheduleprojectdevelop.comment.entity;

import com.enolj.scheduleprojectdevelop.comment.dto.CreateCommentRequest;
import com.enolj.scheduleprojectdevelop.global.common.BaseEntity;
import com.enolj.scheduleprojectdevelop.schedule.entity.Schedule;
import com.enolj.scheduleprojectdevelop.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public static Comment from(User user, Schedule schedule, CreateCommentRequest request) {
        return Comment.builder()
                .description(request.getDescription())
                .user(user)
                .schedule(schedule)
                .build();
    }

    public String getUserName() {
        return this.user.getName();
    }
}
