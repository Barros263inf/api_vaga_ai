package com.vaga.ai.gs.service;

import com.vaga.ai.gs.dto.request.UserRequestPostDTO;
import com.vaga.ai.gs.dto.request.UserRequestUpdateDTO;
import com.vaga.ai.gs.dto.response.UserResponseDTO;
import com.vaga.ai.gs.exception.BusinessRuleException;
import com.vaga.ai.gs.exception.ResourceNotFoundException;
import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.model.enums.Role;
import com.vaga.ai.gs.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

    private String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getMessage("user.not.found", id)
                ));
    }

    @Transactional
    public UserResponseDTO save(UserRequestPostDTO newUser) {
        if (userRepository.existsByEmail(newUser.email())) {
            throw new BusinessRuleException(getMessage("{user.email.duplicate}", newUser, LocaleContextHolder.getLocale()));
        }

        User user = new User();
        user.setName(newUser.name());
        user.setEmail(newUser.email());
        user.setPhone(newUser.phone());

        String encryptedPassword = passwordEncoder.encode(newUser.password());
        user.setPassword(encryptedPassword);

        user.setRole(Role.USER);

        User userSaved = userRepository.save(user);

        return UserResponseDTO.fromEntity(userSaved);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(getMessage("{user.not.found}", id));
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestUpdateDTO updatedUser) {

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

        User userAttach = userRepository.save(existingUser);

        return UserResponseDTO.fromEntity(userAttach);
    }
}
