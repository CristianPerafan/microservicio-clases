package com.icesi.microservicio_clase.config;

import com.icesi.microservicio_clase.dto.ResumenEntrenamiento;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, ResumenEntrenamiento> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "resumen-grupo");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Deserializador para claves String
        StringDeserializer keyDeserializer = new StringDeserializer();
        // Deserializador para valores ResumenEntrenamiento
        JsonDeserializer<ResumenEntrenamiento> valueDeserializer =
                new JsonDeserializer<>(ResumenEntrenamiento.class);
        valueDeserializer.addTrustedPackages("com.icesi.microservicio_clase.dto", "com.icesi.microservicio_clase.model", "com.icesi.microservicio_clase.consumer");

        return new DefaultKafkaConsumerFactory<>(props, keyDeserializer, valueDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ResumenEntrenamiento> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ResumenEntrenamiento> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

}