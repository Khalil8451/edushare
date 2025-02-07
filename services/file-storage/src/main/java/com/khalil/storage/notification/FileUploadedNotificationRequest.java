package com.khalil.storage.notification;

import com.khalil.storage.user.UserResponse;

public record FileUploadedNotificationRequest (
    String fileName,
    UserResponse user
)
{}
