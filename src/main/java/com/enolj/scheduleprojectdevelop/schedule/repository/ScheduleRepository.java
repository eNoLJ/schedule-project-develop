package com.enolj.scheduleprojectdevelop.schedule.repository;

import com.enolj.scheduleprojectdevelop.schedule.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query(
            value = """
        select s as schedule, count(c.id) as commentCount
        from Schedule s
        left join Comment c on c.schedule = s
        where (:author is null or s.user.name = :author)
        group by s
    """,
            countQuery = """
        select count(s)
        from Schedule s
        where (:author is null or s.user.name = :author)
    """
    )
    Page<ScheduleWithCommentCount> findAllWithCommentCount(
            @Param("author") String author,
            Pageable pageable
    );
}
