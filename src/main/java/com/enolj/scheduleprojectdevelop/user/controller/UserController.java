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

    // 회원가입 API
    // 요청 DTO에 대해 @Valid로 유효성 검증 수행
    // 회원가입 성공 시 201 Created 반환
    @PostMapping("/users")
    public ResponseEntity<SignupUserResponse> signup(@Valid @RequestBody SignupUserRequest request) {
        SignupUserResponse response = userService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 로그인 API
    // 이메일, 비밀번호 검증
    // 로그인 성공 시 세션에 로그인 사용자 정보 저장
    // 세션 기반 인증을 위한 loginUser 속성 등록
    @PostMapping("/login")
    public ResponseEntity<SessionUser> login(@Valid @RequestBody LoginUserRequest request, HttpSession session) {
        SessionUser sessionUser = userService.login(request);
        session.setAttribute("loginUser", sessionUser);
        return ResponseEntity.ok(sessionUser);
    }

    // 로그아웃 API
    // 세션에 저장된 로그인 사용자 정보 제거
    // 세션 무효화 처리
    // 로그사웃 성공 시 204 No Content 반환
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            HttpSession session
    ) {
        session.invalidate();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 전체 회원 조회 API
    // 모든 사용자 목록 반환
    @GetMapping("/users")
    public ResponseEntity<List<GetUsersResponse>> getAll() {
        List<GetUsersResponse> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    // 회원 단건 조회 API
    // 세션에 저장된 로그인 사용자 정보를 기반으로 조회
    // URL에 userId가 있지만 실제 조회는 세션 사용자 기준
    @GetMapping("/users/{userId}")
    public ResponseEntity<GetUserResponse> getOne(@SessionAttribute(name = "loginUser") SessionUser sessionUser) {
        GetUserResponse response = userService.findOne(sessionUser);
        return ResponseEntity.ok(response);
    }

    // 회원 정보 수정 API
    // 로그인한 사용자만 자신의 정보 수정 가능
    // @Valid를 통해 요청 데이터 검증
    @PatchMapping("/users")
    public ResponseEntity<UpdateUserResponse> update(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UpdateUserResponse response = userService.update(sessionUser, request);
        return ResponseEntity.ok(response);
    }

    // 회원 탈퇴 API
    // 로그인한 사용자만 탈퇴 가능
    // 비밀번호 검증 등 서비스 레이어에서 처리
    // 성공 시 204 No Content 반환
    @DeleteMapping("/users")
    public ResponseEntity<Void> delete(
            @SessionAttribute(name = "loginUser") SessionUser sessionUser,
            @Valid @RequestBody DeleteUserRequest request
    ) {
        userService.delete(sessionUser, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
