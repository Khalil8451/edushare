package com.khalil.management.user.dto;

public record UserResponse(
        Integer id,
        String firstName,
        String lastName,
        String email,
        Integer groupId,
        String groupName,
        String goupClass
) {
}
