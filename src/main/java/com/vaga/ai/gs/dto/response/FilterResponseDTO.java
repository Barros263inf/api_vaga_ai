package com.vaga.ai.gs.dto.response;

import com.vaga.ai.gs.model.Filter;
import com.vaga.ai.gs.model.enums.JobType;
import com.vaga.ai.gs.model.enums.RemotePreference;

import java.math.BigDecimal;

public record FilterResponseDTO(
        Long id,
        Long userId,
        String title,
        String location,
        JobType jobType,
        BigDecimal salaryMin,
        BigDecimal salaryMax,
        RemotePreference remotePreference,
        String experienceLevel
) {
    public static FilterResponseDTO fromEntity(Filter filter) {
        return new FilterResponseDTO(
                filter.getId(),
                filter.getUser().getId(),
                filter.getTitle(),
                filter.getLocation(),
                filter.getJobType(),
                filter.getSalaryMin(),
                filter.getSalaryMax(),
                filter.getRemotePreference(),
                filter.getExperienceLevel()
        );
    }
}