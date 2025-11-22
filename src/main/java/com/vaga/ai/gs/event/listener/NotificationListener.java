package com.vaga.ai.gs.event.listener;

import com.vaga.ai.gs.event.NotificationEvent;
import com.vaga.ai.gs.messaging.EmailProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @Autowired
    private EmailProducer emailProducer;

    @Async
    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        System.out.println("👂 Evento capturado! Enviando para fila...");
        emailProducer.sendEmailMessage(event.getEmailDTO());
    }
}