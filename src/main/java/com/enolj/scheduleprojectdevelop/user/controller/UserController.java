package com.enolj.scheduleprojectdevelop.user.controller;

import com.enolj.scheduleprojectdevelop.user.dto.GetUserResponse;
import com.enolj.scheduleprojectdevelop.user.dto.GetUsersResponse;
import com.enolj.scheduleprojectdevelop.user.dto.SignupUserRequest;
import com.enolj.scheduleprojectdevelop.user.dto.SignupUserResponse;
import com.enolj.scheduleprojectdevelop.user.service.UserService;
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

    @GetMapping("/users")
    public ResponseEntity<List<GetUsersResponse>> getAll() {
        List<GetUsersResponse> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<GetUserResponse> getOne(@PathVariable Long userId) {
        GetUserResponse response = userService.findOne(userId);
        return ResponseEntity.ok(response);
    }
}
