package com.vaga.ai.gs.service;

import com.vaga.ai.gs.dto.request.JobRequestDTO;
import com.vaga.ai.gs.dto.response.JobResponseDTO;
import com.vaga.ai.gs.exception.BusinessRuleException;
import com.vaga.ai.gs.exception.ResourceNotFoundException;
import com.vaga.ai.gs.model.Job;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public Page<JobResponseDTO> findAll(User loggedUser, Pageable pageable) {
        return jobRepository.findAllByUser(loggedUser, pageable)
                .map(JobResponseDTO::fromEntity);
    }

    public JobResponseDTO findById(Long id, User loggedUser) {
        Job job = findJobOwnedByUser(id, loggedUser);
        return JobResponseDTO.fromEntity(job);
    }

    @Transactional
    public JobResponseDTO save(JobRequestDTO dto, User loggedUser) {
        // Regra: Evitar duplicidade de favoritos
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

        return JobResponseDTO.fromEntity(jobRepository.save(job));
    }

    @Transactional
    public void delete(Long id, User loggedUser) {
        Job job = findJobOwnedByUser(id, loggedUser);
        jobRepository.delete(job);
    }

    // Validação de segurança: Garante que a vaga pertence ao usuário logado
    private Job findJobOwnedByUser(Long id, User user) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("job.not.found", id)));

        if (!job.getUser().getId().equals(user.getId())) {
            throw new BusinessRuleException(getMessage("security.access.denied"));
        }
        return job;
    }
}