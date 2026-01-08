package com.enolj.scheduleprojectdevelop.schedule.entity;

import com.enolj.scheduleprojectdevelop.global.common.BaseEntity;
import com.enolj.scheduleprojectdevelop.schedule.dto.CreateScheduleRequest;
import com.enolj.scheduleprojectdevelop.schedule.dto.UpdateScheduleRequest;
import com.enolj.scheduleprojectdevelop.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, nullable = false)
    private String title;
    @Column(length = 200, nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static Schedule from(User user, CreateScheduleRequest request) {
        return Schedule.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(user)
                .build();
    }

    public void update(UpdateScheduleRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
    }

    public boolean matchesUser(User user) {
        return this.user.equals(user);
    }

    public String getUserName() {
        return this.user.getName();
    }
}
