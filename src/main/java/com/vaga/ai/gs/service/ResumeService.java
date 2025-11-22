package com.vaga.ai.gs.service;

import com.vaga.ai.gs.dto.messaging.EmailDTO;
import com.vaga.ai.gs.dto.request.ResumeRequestDTO;
import com.vaga.ai.gs.event.NotificationEvent;
import com.vaga.ai.gs.exception.BusinessRuleException;
import com.vaga.ai.gs.exception.ResourceNotFoundException;
import com.vaga.ai.gs.model.Resume;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public Page<Resume> findAll(User loggedUser, Pageable pageable) {
        return resumeRepository.findAllByUser(loggedUser, pageable);
    }

    public Resume findById(Long id, User loggedUser) {
        return findResumeOwnedByUser(id, loggedUser);
    }

    @Transactional
    public Resume save(ResumeRequestDTO dto, User loggedUser) {
        Resume resume = new Resume();
        resume.setUser(loggedUser);
        resume.setFileName(dto.fileName());
        resume.setFilePath(dto.filePath());
        resume.setExtractedText(dto.extractedText());
        resume.setExtractedSkills(dto.extractedSkills());

        Resume savedResume = resumeRepository.save(resume);

        EmailDTO email = new EmailDTO(
                loggedUser.getEmail(),
                "Currículo Recebido!",
                "Olá " + loggedUser.getName() + ", recebemos seu currículo (" + savedResume.getFileName() + ") e ele já está em análise pela nossa IA."
        );
        eventPublisher.publishEvent(new NotificationEvent(this, email));

        return savedResume;
    }

    @Transactional
    public void delete(Long id, User loggedUser) {
        Resume resume = findResumeOwnedByUser(id, loggedUser);
        resumeRepository.delete(resume);
    }

    // Validação de segurança
    private Resume findResumeOwnedByUser(Long id, User user) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getMessage("resume.not.found", id)));

        if (!resume.getUser().getId().equals(user.getId())) {
            throw new BusinessRuleException(getMessage("security.access.denied"));
        }
        return resume;
    }
}