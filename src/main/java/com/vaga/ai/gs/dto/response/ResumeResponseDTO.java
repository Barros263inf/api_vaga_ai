package com.vaga.ai.gs.dto.response;

import com.vaga.ai.gs.model.Resume;
import java.time.LocalDateTime;

public record ResumeResponseDTO(
        Long id,
        String fileName,
        String filePath,
        String extractedText,
        String extractedSkills,
        LocalDateTime createdAt
) {
    public static ResumeResponseDTO fromEntity(Resume resume) {
        return new ResumeResponseDTO(
                resume.getId(),
                resume.getFileName(),
                resume.getFilePath(),
                resume.getExtractedText(),
                resume.getExtractedSkills(),
                resume.getCreatedAt()
        );
    }
}