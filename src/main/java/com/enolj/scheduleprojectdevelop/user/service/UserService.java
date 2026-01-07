package com.enolj.scheduleprojectdevelop.user.service;

import com.enolj.scheduleprojectdevelop.global.exception.ErrorCode;
import com.enolj.scheduleprojectdevelop.user.dto.GetUserResponse;
import com.enolj.scheduleprojectdevelop.user.dto.GetUsersResponse;
import com.enolj.scheduleprojectdevelop.user.dto.SignupUserRequest;
import com.enolj.scheduleprojectdevelop.user.dto.SignupUserResponse;
import com.enolj.scheduleprojectdevelop.user.entity.User;
import com.enolj.scheduleprojectdevelop.user.exception.DuplicateEmailException;
import com.enolj.scheduleprojectdevelop.user.exception.UserNotFoundException;
import com.enolj.scheduleprojectdevelop.user.repository.UserRepository;
import jakarta.validation.Valid;
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
    public GetUserResponse findOne(Long userId) {
        User user = findById(userId);
        return GetUserResponse.from(user);
    }

    private User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND)
        );
    }
}
