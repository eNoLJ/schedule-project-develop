package com.enolj.scheduleprojectdevelop.comment.service;

import com.enolj.scheduleprojectdevelop.comment.dto.CreateCommentRequest;
import com.enolj.scheduleprojectdevelop.comment.dto.CreateCommentResponse;
import com.enolj.scheduleprojectdevelop.comment.entity.Comment;
import com.enolj.scheduleprojectdevelop.comment.repository.CommentRepository;
import com.enolj.scheduleprojectdevelop.schedule.entity.Schedule;
import com.enolj.scheduleprojectdevelop.schedule.service.ScheduleService;
import com.enolj.scheduleprojectdevelop.user.dto.SessionUser;
import com.enolj.scheduleprojectdevelop.user.entity.User;
import com.enolj.scheduleprojectdevelop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ScheduleService scheduleService;

    @Transactional
    public CreateCommentResponse save(SessionUser sessionUser, Long scheduleId, CreateCommentRequest request) {
        User user = userService.findById(sessionUser.getId());
        Schedule schedule = scheduleService.findById(scheduleId);
        Comment comment = Comment.from(user, schedule, request);
        commentRepository.save(comment);
        return CreateCommentResponse.from(comment);
    }
}
