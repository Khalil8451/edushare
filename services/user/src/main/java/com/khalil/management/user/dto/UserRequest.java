package com.khalil.management.user.dto;

import jakarta.validation.constraints.NotNull;

public record UserRequest(

        Integer id,
        @NotNull(message = "User firstname is required")
        String firstName,
        @NotNull(message = "User lastname is required")
        String lastName,
        @NotNull(message = "User email is required")
        String email,
        @NotNull(message = "User group is required")
        Integer groupId
) {
}
