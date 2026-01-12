package com.enolj.scheduleprojectdevelop.comment.service;

import com.enolj.scheduleprojectdevelop.comment.dto.CreateCommentRequest;
import com.enolj.scheduleprojectdevelop.comment.dto.CreateCommentResponse;
import com.enolj.scheduleprojectdevelop.comment.dto.GetCommentsResponse;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ScheduleService scheduleService;

    // 댓글 생성
    // 로그인한 사용자를 작성자로 설정
    // 댓글이 달릴 일정을 조회
    // 댓글 엔티티 생성 후 저장
    @Transactional
    public CreateCommentResponse save(SessionUser sessionUser, Long scheduleId, CreateCommentRequest request) {
        User user = userService.findById(sessionUser.getId());
        Schedule schedule = scheduleService.findById(scheduleId);
        Comment comment = Comment.from(user, schedule, request);
        commentRepository.save(comment);
        return CreateCommentResponse.from(comment);
    }

    // 특정 일정에 달린 댓글 목록 조회
    // 일정 ID를 기준으로 댓글 목록 조회
    public List<GetCommentsResponse> findAll(Long scheduleId) {
        List<Comment> comments = commentRepository.findAllByScheduleId(scheduleId);
        return comments.stream()
                .map(GetCommentsResponse::from)
                .toList();
    }
}
