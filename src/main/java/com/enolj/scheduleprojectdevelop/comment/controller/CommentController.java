package com.enolj.scheduleprojectdevelop.comment.controller;

import com.enolj.scheduleprojectdevelop.comment.dto.CreateCommentRequest;
import com.enolj.scheduleprojectdevelop.comment.dto.CreateCommentResponse;
import com.enolj.scheduleprojectdevelop.comment.dto.GetCommentsResponse;
import com.enolj.scheduleprojectdevelop.comment.service.CommentService;
import com.enolj.scheduleprojectdevelop.user.dto.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성 API
    // 로그인한 사용자만 댓글 생성 가능
    // 댓글 생성 시 201 created 반환
    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CreateCommentResponse> create(
            @SessionAttribute(name = "loginUser")SessionUser sessionUser,
            @PathVariable Long scheduleId,
            @Valid @RequestBody CreateCommentRequest request
            ) {
        CreateCommentResponse response = commentService.save(sessionUser, scheduleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 일정에 달린 댓글 조회 API
    // 일정 Id를 PathVariable로 받아 해당 일정에 달린 댓글 조회
    @GetMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<List<GetCommentsResponse>> getAll(@PathVariable Long scheduleId) {
        List<GetCommentsResponse> responseList = commentService.findAll(scheduleId);
        return ResponseEntity.ok(responseList);
    }
}
