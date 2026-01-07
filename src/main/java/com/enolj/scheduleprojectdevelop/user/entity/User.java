package com.enolj.scheduleprojectdevelop.user.entity;

import com.enolj.scheduleprojectdevelop.global.common.BaseEntity;
import com.enolj.scheduleprojectdevelop.user.dto.SignupUserRequest;
import com.enolj.scheduleprojectdevelop.user.dto.UpdateUserRequest;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    public static User from(SignupUserRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public boolean matchesPassword(String currentPassword) {
        return this.password.equals(currentPassword);
    }

    public void update(UpdateUserRequest request) {
        this.name = request.getName() == null ? this.name : request.getName();
        this.password = request.getNewPassword() == null ? this.password : request.getNewPassword();
    }
}
