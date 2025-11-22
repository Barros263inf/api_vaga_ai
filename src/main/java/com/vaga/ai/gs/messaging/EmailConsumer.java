package com.vaga.ai.gs.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaga.ai.gs.config.RabbitMQConfig;
import com.vaga.ai.gs.dto.messaging.EmailDTO;
import com.vaga.ai.gs.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveEmail(@Payload String json) {
        try {
            EmailDTO dto = objectMapper.readValue(json, EmailDTO.class);
            System.out.println("---------------------------------");
            System.out.println("📧 E-MAIL ENVIADO (Simulação):");
            System.out.println("Para: " + dto.to());
            System.out.println("Assunto: " + dto.subject());
            System.out.println("Corpo: " + dto.body());
            System.out.println("---------------------------------");

            notificationService.sendNotification(dto.to(), dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
