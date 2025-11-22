package com.vaga.ai.gs.service;

import com.vaga.ai.gs.dto.request.FilterRequestPostDTO;
import com.vaga.ai.gs.dto.request.FilterRequestUpdateDTO;
import com.vaga.ai.gs.dto.response.FilterResponseDTO;
import com.vaga.ai.gs.exception.ResourceNotFoundException;
import com.vaga.ai.gs.exception.BusinessRuleException;
import com.vaga.ai.gs.model.Filter;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.repository.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FilterService {

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private MessageSource messageSource;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public Page<FilterResponseDTO> findAll(User loggedUser, Pageable pageable) {

        return filterRepository.findAllByUser(loggedUser, pageable)
                .map(FilterResponseDTO::fromEntity);
    }

    public FilterResponseDTO findById(Long id, User loggedUser) {
        Filter filter = findFilterOwnedByUser(id, loggedUser);
        return FilterResponseDTO.fromEntity(filter);
    }

    @Transactional
    public FilterResponseDTO save(FilterRequestPostDTO dto, User loggedUser) {
        Filter filter = new Filter();
        filter.setUser(loggedUser);
        filter.setTitle(dto.title());
        filter.setLocation(dto.location());
        filter.setJobType(dto.jobType());
        filter.setSalaryMin(dto.salaryMin());
        filter.setSalaryMax(dto.salaryMax());
        filter.setRemotePreference(dto.remotePreference());
        filter.setExperienceLevel(dto.experienceLevel());

        return FilterResponseDTO.fromEntity(filterRepository.save(filter));
    }

    @Transactional
    public FilterResponseDTO update(Long id, FilterRequestUpdateDTO dto, User loggedUser) {
        Filter filter = findFilterOwnedByUser(id, loggedUser);

        if (dto.title() != null && !dto.title().isBlank()) filter.setTitle(dto.title());
        if (dto.location() != null && !dto.location().isBlank()) filter.setLocation(dto.location());
        if (dto.jobType() != null) filter.setJobType(dto.jobType());
        if (dto.salaryMin() != null) filter.setSalaryMin(dto.salaryMin());
        if (dto.salaryMax() != null) filter.setSalaryMax(dto.salaryMax());
        if (dto.remotePreference() != null) filter.setRemotePreference(dto.remotePreference());
        if (dto.experienceLevel() != null && !dto.experienceLevel().isBlank())
            filter.setExperienceLevel(dto.experienceLevel());

        return FilterResponseDTO.fromEntity(filterRepository.save(filter));
    }

    @Transactional
    public void delete(Long id, User loggedUser) {
        Filter filter = findFilterOwnedByUser(id, loggedUser);
        filterRepository.delete(filter);
    }

    private Filter findFilterOwnedByUser(Long id, User user) {
        Filter filter = filterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("filter.not.found", id)));

        if (!filter.getUser().getId().equals(user.getId())) {
            throw new BusinessRuleException(getMessage("security.access.denied"));
        }
        return filter;
    }
}