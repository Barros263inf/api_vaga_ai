package com.vaga.ai.gs.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    // HashMap to link email and notification emitter
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String email) {
        // 1Hr timeout for reconnect
        SseEmitter emitter = new SseEmitter(3600000L);

        emitterMap.put(email, emitter);

        System.out.println("📢 Usuário conectado para notificações: " + email);

        // Callback to clear death connections
        emitter.onCompletion(() -> emitterMap.remove(email));
        emitter.onTimeout(() -> emitterMap.remove(email));
        emitter.onError((e) -> emitterMap.remove(email));

        return emitter;
    }

    public void sendNotification(String email, Object payload) {
        SseEmitter emitter = emitterMap.get(email);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .data("notification")
                        .data(payload));
            } catch (IOException e) {
                emitterMap.remove(email);
            }
        }
    }
}
