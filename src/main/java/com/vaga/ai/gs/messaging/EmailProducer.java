package com.vaga.ai.gs.messaging;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaga.ai.gs.config.RabbitMQConfig;
import com.vaga.ai.gs.dto.messaging.EmailDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendEmailMessage(EmailDTO emailDTO) {
        try {
            String json = objectMapper.writeValueAsString(emailDTO);
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, json);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
