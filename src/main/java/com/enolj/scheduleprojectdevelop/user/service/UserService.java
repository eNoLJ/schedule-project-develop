package com.enolj.scheduleprojectdevelop.user.service;

import com.enolj.scheduleprojectdevelop.global.config.PasswordEncoder;
import com.enolj.scheduleprojectdevelop.global.exception.ErrorCode;
import com.enolj.scheduleprojectdevelop.user.dto.*;
import com.enolj.scheduleprojectdevelop.user.entity.User;
import com.enolj.scheduleprojectdevelop.user.exception.DuplicateEmailException;
import com.enolj.scheduleprojectdevelop.user.exception.LoginFailedException;
import com.enolj.scheduleprojectdevelop.user.exception.NotMatchPasswordException;
import com.enolj.scheduleprojectdevelop.user.exception.UserNotFoundException;
import com.enolj.scheduleprojectdevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    // 이메일 중복여부 먼저 검증
    // 비밀번호를 암호화하여 회원 정보를 저장
    @Transactional
    public SignupUserResponse save(SignupUserRequest request) {
        boolean existence = userRepository.existsUserByEmail(request.getEmail());
        if (existence) {
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);
        }
        User user = User.from(request, passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return SignupUserResponse.from(user);
    }

    // 로그인
    // 이메일로 회원 조회
    // 입력한 비밀번호와 저장된 암호화 비밀번호를 비교
    @Transactional(readOnly = true)
    public SessionUser login(LoginUserRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new LoginFailedException(ErrorCode.LOGIN_FAILED)
        );

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new LoginFailedException(ErrorCode.LOGIN_FAILED);
        }

        return SessionUser.from(user);
    }

    // 전체 회원 조회
    // 수정일 기준 내림차순 정렬
    @Transactional(readOnly = true)
    public List<GetUsersResponse> findAll() {
        List<User> users = userRepository.findAllByOrderByModifiedAtDesc();
        return users.stream()
                .map(GetUsersResponse::from)
                .toList();
    }

    // 로그인한 회원 단건 조회
    // 세션에 저장된 회원 ID 기준
    @Transactional(readOnly = true)
    public GetUserResponse findOne(SessionUser sessionUser) {
        User user = findById(sessionUser.getId());
        return GetUserResponse.from(user);
    }

    // 회원 정보 수정
    // 현재 비밀번호 검증 후 수정 가능
    // 새 비밀번호는 다시 안호화
    @Transactional
    public UpdateUserResponse update(SessionUser sessionUser, UpdateUserRequest request) {
        User user = findById(sessionUser.getId());
        validatePassword(user, request.getCurrentPassword());
        String newPassword = passwordEncoder.encode(request.getNewPassword());
        user.update(request, newPassword);

        // 수정일 적용을 위한 flush()
        userRepository.flush();
        return UpdateUserResponse.from(user);
    }

    // 회원 삭제
    // 비밀번호 검증 후 삭제 가능
    @Transactional
    public void delete(SessionUser sessionUser, DeleteUserRequest request) {
        User user = findById(sessionUser.getId());
        validatePassword(user, request.getPassword());
        userRepository.delete(user);
    }

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
        );
    }

    private void validatePassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new NotMatchPasswordException(ErrorCode.NOT_MATCH_PASSWORD);
        }
    }
}
