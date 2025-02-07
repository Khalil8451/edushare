package com.khalil.notification.email;

import lombok.Getter;

public enum EmailTemplates {

    FILE_UPLOADED("file-uploaded.html", "file successfully uploaded");

    @Getter
    private final String template;
    @Getter
    private final String subject;


    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
