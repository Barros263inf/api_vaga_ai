package com.vaga.ai.gs.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ResumeRequestDTO(
        @NotBlank(message = "{resume.filename.required}")
        String fileName,

        String filePath,

        @NotBlank(message = "{resume.text.required}")
        String extractedText,

        String extractedSkills // Opcional, pois a IA pode gerar depois
) {}