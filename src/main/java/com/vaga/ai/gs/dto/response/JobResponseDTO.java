package com.vaga.ai.gs.dto.response;

import com.vaga.ai.gs.model.Job;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record JobResponseDTO(
        Long id,
        String jobApiId,
        String jobTitle,
        String companyName,
        String location,
        String description,
        BigDecimal salaryInfo,
        String redirectUrl,
        LocalDateTime savedAt
) {
    public static JobResponseDTO fromEntity(Job job) {
        return new JobResponseDTO(
                job.getId(),
                job.getJobApiId(),
                job.getJobTitle(),
                job.getCompanyName(),
                job.getLocation(),
                job.getDescription(),
                job.getSalaryInfo(),
                job.getRedirectUrl(),
                job.getSavedAt()
        );
    }
}