package com.vaga.ai.gs.dto.request;

import com.vaga.ai.gs.model.enums.JobType;
import com.vaga.ai.gs.model.enums.RemotePreference;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record FilterRequestUpdateDTO(
        String title,
        String location,
        JobType jobType,
        @Positive BigDecimal salaryMin,
        @Positive BigDecimal salaryMax,
        RemotePreference remotePreference,
        String experienceLevel
) {}