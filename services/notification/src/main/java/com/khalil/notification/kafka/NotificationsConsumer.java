package com.khalil.notification.kafka;

import com.khalil.notification.email.EmailService;
import com.khalil.notification.kafka.file.FileUploaded;
import com.khalil.notification.notification.Notification;
import com.khalil.notification.notification.NotificationRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.khalil.notification.notification.NotificationType.FILE_UPLOADED;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationsConsumer {

    private final NotificationRepository repository;
    private final EmailService emailService;
    @KafkaListener(topics = "file-topic")
    public void consumeFileUploadedSuccessNotifications(FileUploaded fileUploaded) throws MessagingException {
        log.info(format("Consuming the message from file-topic Topic:: %s", fileUploaded));
        repository.save(
                Notification.builder()
                        .type(FILE_UPLOADED)
                        .notificationDate(LocalDateTime.now())
                        .fileUploaded(fileUploaded)
                        .build()
        );
        var userName = fileUploaded.user().firstName() + " " + fileUploaded.user().lastName();
        emailService.sendFileUploadedSuccessEmail(
                fileUploaded.user().email(),
                userName,
                fileUploaded.fileName()
        );
    }
}
