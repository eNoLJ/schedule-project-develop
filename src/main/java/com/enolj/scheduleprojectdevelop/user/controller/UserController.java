package com.enolj.scheduleprojectdevelop.user.controller;

import com.enolj.scheduleprojectdevelop.user.dto.*;
import com.enolj.scheduleprojectdevelop.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<SignupUserResponse> signup(@Valid @RequestBody SignupUserRequest request) {
        SignupUserResponse response = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<SessionUser> login(@Valid @RequestBody LoginUserRequest request, HttpSession session) {
        SessionUser sessionUser = userService.login(request);
        session.setAttribute("loginUser", sessionUser);
        return ResponseEntity.ok(sessionUser);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            HttpSession session
    ) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<GetUsersResponse>> getAll() {
        List<GetUsersResponse> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<GetUserResponse> getOne(@SessionAttribute(name = "loginUser") SessionUser sessionUser) {
        GetUserResponse response = userService.findOne(sessionUser);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<UpdateUserResponse> update(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UpdateUserResponse response = userService.update(sessionUser, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> delete(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @Valid @RequestBody DeleteUserRequest request
    ) {
        userService.delete(sessionUser, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
