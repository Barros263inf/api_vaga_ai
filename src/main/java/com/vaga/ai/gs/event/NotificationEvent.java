package com.vaga.ai.gs.event;

import com.vaga.ai.gs.dto.messaging.EmailDTO;
import org.springframework.context.ApplicationEvent;

public class NotificationEvent extends ApplicationEvent {

    private final EmailDTO emailDTO;

    public NotificationEvent(Object source, EmailDTO emailDTO) {
        super(source);
        this.emailDTO = emailDTO;
    }

    public EmailDTO getEmailDTO() {
        return emailDTO;
    }
}