package com.enolj.scheduleprojectdevelop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ScheduleProjectDevelopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleProjectDevelopApplication.class, args);
    }

}
