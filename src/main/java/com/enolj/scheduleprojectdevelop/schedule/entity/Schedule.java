package com.enolj.scheduleprojectdevelop.schedule.entity;

import com.enolj.scheduleprojectdevelop.global.common.BaseEntity;
import com.enolj.scheduleprojectdevelop.schedule.dto.CreateScheduleRequest;
import com.enolj.scheduleprojectdevelop.schedule.dto.UpdateScheduleRequest;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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
    @Column(nullable = false)
    private String author;

    public static Schedule from(CreateScheduleRequest request) {
        return Schedule.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .author(request.getAuthor())
                .build();
    }

    public void update(UpdateScheduleRequest request) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.author = request.getAuthor();
    }
}
