package com.icesi.microservicio_clase.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic ocupacionClaseTopic() {
        return TopicBuilder.name("ocupacion.clases").build();
    }

}
