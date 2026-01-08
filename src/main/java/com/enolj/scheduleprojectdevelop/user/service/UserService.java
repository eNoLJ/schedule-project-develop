package com.enolj.scheduleprojectdevelop.user.service;

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

    @Transactional
    public SignupUserResponse save(SignupUserRequest request) {
        boolean existence = userRepository.existsUserByEmail(request.getEmail());
        if (existence) {
            throw new DuplicateEmailException(ErrorCode.DUPLICATE_EMAIL);
        }
        User user = User.from(request);
        userRepository.save(user);
        return SignupUserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public List<GetUsersResponse> findAll() {
        List<User> users = userRepository.findAllByOrderByModifiedAtDesc();
        return users.stream()
                .map(GetUsersResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GetUserResponse findOne(SessionUser sessionUser) {
        User user = findById(sessionUser.getId());
        return GetUserResponse.from(user);
    }

    @Transactional
    public UpdateUserResponse update(SessionUser sessionUser, UpdateUserRequest request) {
        User user = findById(sessionUser.getId());
        validatePassword(user, request.getCurrentPassword());
        user.update(request);

        // 수정일 적용을 위한 flush()
        userRepository.flush();
        return UpdateUserResponse.from(user);
    }

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
        if (!user.matchesPassword(password)) {
            throw new NotMatchPasswordException(ErrorCode.NOT_MATCH_PASSWORD);
        }
    }

    public SessionUser login(LoginUserRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElseThrow(
                () -> new LoginFailedException(ErrorCode.LOGIN_FAILED)
        );
        return SessionUser.from(user);
    }
}
