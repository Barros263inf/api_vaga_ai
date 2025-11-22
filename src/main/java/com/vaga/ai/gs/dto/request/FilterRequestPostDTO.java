package com.vaga.ai.gs.dto.request;

import com.vaga.ai.gs.model.enums.JobType;
import com.vaga.ai.gs.model.enums.RemotePreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record FilterRequestPostDTO(
        @NotBlank(message = "{filter.title.required}")
        String title,

        @NotBlank(message = "{filter.location.required}")
        String location,

        JobType jobType, // Opcional, mas se vier, deve ser válido

        @Positive(message = "{filter.salary.positive}")
        BigDecimal salaryMin,

        @Positive(message = "{filter.salary.positive}")
        BigDecimal salaryMax,

        RemotePreference remotePreference,

        String experienceLevel
) {}