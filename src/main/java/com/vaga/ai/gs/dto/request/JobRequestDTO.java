package com.vaga.ai.gs.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record JobRequestDTO(
        @NotBlank(message = "{job.api_id.required}")
        String jobApiId,

        @NotBlank(message = "{job.title.required}")
        String jobTitle,

        String companyName,

        @NotBlank(message = "{job.location.required}")
        String location,

        String description,

        @Positive(message = "{job.salary.positive}")
        BigDecimal salaryInfo,

        @NotBlank(message = "{job.url.required}")
        String redirectUrl
) {}