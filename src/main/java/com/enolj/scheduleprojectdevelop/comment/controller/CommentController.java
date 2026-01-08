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

    @PostMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<CreateCommentResponse> create(
            @SessionAttribute(name = "loginUser")SessionUser sessionUser,
            @PathVariable Long scheduleId,
            @Valid @RequestBody CreateCommentRequest request
            ) {
        CreateCommentResponse response = commentService.save(sessionUser, scheduleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/schedules/{scheduleId}/comments")
    public ResponseEntity<List<GetCommentsResponse>> getAll(@PathVariable Long scheduleId) {
        List<GetCommentsResponse> responseList = commentService.findAll(scheduleId);
        return ResponseEntity.ok(responseList);
    }
}
