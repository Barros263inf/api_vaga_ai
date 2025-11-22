package com.vaga.ai.gs.service;

import com.vaga.ai.gs.dto.messaging.EmailDTO;
import com.vaga.ai.gs.dto.request.UserRequestPostDTO;
import com.vaga.ai.gs.dto.request.UserRequestUpdateDTO;
import com.vaga.ai.gs.exception.BusinessRuleException;
import com.vaga.ai.gs.exception.ResourceNotFoundException;
import com.vaga.ai.gs.messaging.EmailProducer;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private EmailProducer emailProducer;

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Cacheable(value = "users", key = "#id")
    public User findById(Long id) {
        System.out.println("🔎 Buscando no banco de dados...");
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getMessage("user.not.found", id)
                ));
    }

    @Transactional
    public User save(UserRequestPostDTO newUser) {
        if (userRepository.existsByEmail(newUser.email())) {
            throw new BusinessRuleException(getMessage("{user.email.duplicate}", newUser, LocaleContextHolder.getLocale()));
        }

        User user = new User();
        user.setName(newUser.name());
        user.setEmail(newUser.email());
        user.setPhone(newUser.phone());

        String encryptedPassword = passwordEncoder.encode(newUser.password());
        user.setPassword(encryptedPassword);

        user.setRole(newUser.role());

        User userSaved = userRepository.save(user);

        try {
            EmailDTO email = new EmailDTO(
                    userSaved.getEmail(),
                    "Bem-vindo ao Vaga AI!",
                    "Olá " + userSaved.getName() + ", o seu cadastro foi realizado com sucesso!"
            );
            emailProducer.sendEmailMessage(email);
        } catch (Exception e) {
            System.err.println("Falha ao enviar notificação: " + e.getMessage());
        }

        return userSaved;
    }

    @Transactional
    @CacheEvict(value = "users", key = "#id")
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(getMessage("{user.not.found}", id));
        }
        userRepository.deleteById(id);
    }

    @Transactional
    @CachePut(value = "users", key = "#id")
    public User update(Long id, UserRequestUpdateDTO updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getMessage("user.not.found", id)
                ));

        if (updatedUser.name() != null && !updatedUser.name().isBlank()) {
            existingUser.setName(updatedUser.name());
        }

        if (updatedUser.email() != null && !updatedUser.email().isBlank()) {
            if (existingUser.getEmail().equals(updatedUser.email())) {
                if (userRepository.existsByEmail(updatedUser.email())) {
                    throw new BusinessRuleException(getMessage("user.email.duplicate"));
                }
            }
            existingUser.setEmail(updatedUser.email());
        }

        if (updatedUser.phone() != null) {
            existingUser.setPhone(updatedUser.phone());
        }

        if (updatedUser.password() != null && !updatedUser.password().isBlank()) {
            String encryptedPassword = passwordEncoder.encode(updatedUser.password());
            existingUser.setPassword(encryptedPassword);
        }

        return userRepository.save(existingUser);
    }
}
