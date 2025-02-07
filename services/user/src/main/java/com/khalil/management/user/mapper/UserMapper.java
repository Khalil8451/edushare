package com.khalil.management.user.mapper;

import com.khalil.management.group.entity.AcademicGroup;
import com.khalil.management.user.dto.UserRequest;
import com.khalil.management.user.dto.UserResponse;
import com.khalil.management.user.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toUser(UserRequest request) {
        return User.builder()
                .id(request.id())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .group(
                        AcademicGroup.builder()
                                .id(request.groupId())
                                .build()
                )
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getGroup().getId(),
                user.getGroup().getName(),
                user.getGroup().getClass_group()
        );
    }
}
