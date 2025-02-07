package com.khalil.notification.kafka.file;

public record FileUploaded (
        String fileName,
        User user
) {

}