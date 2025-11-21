package com.vaga.ai.gs.dto.messaging;

import java.io.Serializable;

public record EmailDTO(
        String to,
        String subject,
        String body
) implements Serializable {}