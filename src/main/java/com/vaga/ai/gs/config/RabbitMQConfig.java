package com.vaga.ai.gs.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "email-boas-vindas";

    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_NAME, true);
    }

}
