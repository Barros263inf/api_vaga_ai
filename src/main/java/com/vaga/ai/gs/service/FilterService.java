package com.vaga.ai.gs.service;

import com.vaga.ai.gs.dto.messaging.EmailDTO;
import com.vaga.ai.gs.dto.request.FilterRequestPostDTO;
import com.vaga.ai.gs.dto.request.FilterRequestUpdateDTO;
import com.vaga.ai.gs.event.NotificationEvent;
import com.vaga.ai.gs.exception.ResourceNotFoundException;
import com.vaga.ai.gs.exception.BusinessRuleException;
import com.vaga.ai.gs.model.Filter;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.repository.FilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
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

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public Page<Filter> findAll(User loggedUser, Pageable pageable) {

        return filterRepository.findAllByUser(loggedUser, pageable);
    }

    @Cacheable(value = "filters", key = "#id")
    public Filter findById(Long id, User loggedUser) {
        System.out.println("🔎 Buscando no banco de dados...");
        return findFilterOwnedByUser(id, loggedUser);
    }

    @Transactional
    public Filter save(FilterRequestPostDTO dto, User loggedUser) {
        Filter filter = new Filter();
        filter.setUser(loggedUser);
        filter.setTitle(dto.title());
        filter.setLocation(dto.location());
        filter.setJobType(dto.jobType());
        filter.setSalaryMin(dto.salaryMin());
        filter.setSalaryMax(dto.salaryMax());
        filter.setRemotePreference(dto.remotePreference());
        filter.setExperienceLevel(dto.experienceLevel());

        EmailDTO email = new EmailDTO(
                loggedUser.getEmail(),
                "Novo Filtro Criado",
                "Você criou o filtro: " + filter.getTitle()
        );

        eventPublisher.publishEvent(new NotificationEvent(this, email));

        return filterRepository.save(filter);
    }

    @Transactional
    @CachePut(value = "filters", key = "#id")
    public Filter update(Long id, FilterRequestUpdateDTO dto, User loggedUser) {
        Filter filter = findFilterOwnedByUser(id, loggedUser);

        if (dto.title() != null && !dto.title().isBlank()) filter.setTitle(dto.title());
        if (dto.location() != null && !dto.location().isBlank()) filter.setLocation(dto.location());
        if (dto.jobType() != null) filter.setJobType(dto.jobType());
        if (dto.salaryMin() != null) filter.setSalaryMin(dto.salaryMin());
        if (dto.salaryMax() != null) filter.setSalaryMax(dto.salaryMax());
        if (dto.remotePreference() != null) filter.setRemotePreference(dto.remotePreference());
        if (dto.experienceLevel() != null && !dto.experienceLevel().isBlank())
            filter.setExperienceLevel(dto.experienceLevel());

        return filterRepository.save(filter);
    }

    @Transactional
    @CacheEvict(value = "filters", key = "#id")
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