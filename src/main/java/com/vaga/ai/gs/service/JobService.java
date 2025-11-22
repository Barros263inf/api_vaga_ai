package com.vaga.ai.gs.service;

import com.vaga.ai.gs.dto.messaging.EmailDTO;
import com.vaga.ai.gs.dto.request.JobRequestDTO;
import com.vaga.ai.gs.event.NotificationEvent;
import com.vaga.ai.gs.exception.BusinessRuleException;
import com.vaga.ai.gs.exception.ResourceNotFoundException;
import com.vaga.ai.gs.model.Job;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    // Security validation: Ensures that the job opening belongs to the logged-in user.
    private Job findJobOwnedByUser(Long id, User user) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("job.not.found", id)));

        if (!job.getUser().getId().equals(user.getId())) {
            throw new BusinessRuleException(getMessage("security.access.denied"));
        }
        return job;
    }

    public Page<Job> findAll(User loggedUser, Pageable pageable) {
        return jobRepository.findAllByUser(loggedUser, pageable);
    }

    @Cacheable(value = "jobs", key = "#id")
    public Job findById(Long id, User loggedUser) {
        System.out.println("🔎 Buscando no banco de dados...");
        return findJobOwnedByUser(id, loggedUser);
    }

    @Transactional
    public Job save(JobRequestDTO dto, User loggedUser) {
        //Rule: Avoid duplicate favorites.
        if (jobRepository.existsByUserAndJobApiId(loggedUser, dto.jobApiId())) {
            throw new BusinessRuleException(getMessage("job.already.saved"));
        }

        Job job = new Job();
        job.setUser(loggedUser);
        job.setJobApiId(dto.jobApiId());
        job.setJobTitle(dto.jobTitle());
        job.setCompanyName(dto.companyName());
        job.setLocation(dto.location());
        job.setDescription(dto.description());
        job.setSalaryInfo(dto.salaryInfo());
        job.setRedirectUrl(dto.redirectUrl());

        EmailDTO email = new EmailDTO(
                loggedUser.getEmail(),
                "Nova Vaga",
                "Você favoritou: " + job.getJobTitle()
        );

        eventPublisher.publishEvent(new NotificationEvent(this, email));

        return jobRepository.save(job);
    }

    @Transactional
    @CacheEvict(value = "jobs", key = "#id")
    public void delete(Long id, User loggedUser) {
        Job job = findJobOwnedByUser(id, loggedUser);
        jobRepository.delete(job);
    }
}