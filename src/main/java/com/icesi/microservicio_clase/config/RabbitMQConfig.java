package com.icesi.microservicio_clase.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_INSCRIPCION = "inscripcion.exchange";
    public static final String QUEUE_INSCRIPCION = "inscripcion.queue";

    @Bean
    public Queue inscripcionQueue() {
        return new Queue(QUEUE_INSCRIPCION, true);
    }

    @Bean
    public TopicExchange inscripcionExchange() {
        return new TopicExchange(EXCHANGE_INSCRIPCION);
    }

    @Bean
    public Binding binding(Queue inscripcionQueue, TopicExchange inscripcionExchange) {
        return BindingBuilder.bind(inscripcionQueue).to(inscripcionExchange).with("inscripcion.routingkey");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }












}



