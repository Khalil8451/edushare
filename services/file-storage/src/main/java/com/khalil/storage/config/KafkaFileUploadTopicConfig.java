package com.khalil.storage.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaFileUploadTopicConfig {

    @Bean
    public NewTopic fileTopic() {
        return TopicBuilder
                .name("file-topic")
                .build();
    }
}


