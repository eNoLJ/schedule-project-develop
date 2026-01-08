package com.enolj.scheduleprojectdevelop.user.repository;

import com.enolj.scheduleprojectdevelop.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUserByEmail(String email);

    List<User> findAllByOrderByModifiedAtDesc();

    Optional<User> findByEmailAndPassword(String email, String password);
}
