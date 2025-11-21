package com.vaga.ai.gs.service;

import com.vaga.ai.gs.dto.request.UserRequestDTO;
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

    public User findById (Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getMessage("user.not.found", id)
                ));
    }

    @Transactional
    public UserResponseDTO save (UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessRuleException(getMessage("{user.email.duplicate}"));
        }

        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setPhone(dto.phone());

        String encryptedPassword = passwordEncoder.encode(dto.password());
        user.setPassword(encryptedPassword);

        user.setRole(Role.USER);

        User userSaved = userRepository.save(user);

        return UserResponseDTO.fromEntity(userSaved);
    }

    @Transactional
    public void delete (Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(getMessage("{user.not.found}", id));
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponseDTO update (Long id, UserRequestDTO updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getMessage("user.not.found", id)
                ));

        if (!existingUser.getEmail().equals(updatedUser.email()) && userRepository.existsByEmail(updatedUser.email())) {
            throw new BusinessRuleException(getMessage("user.email.duplicate"));
        }

        existingUser.setName(updatedUser.name());
        existingUser.setPhone(updatedUser.phone());
        existingUser.setEmail(updatedUser.email());
        existingUser.setPassword(updatedUser.password());

        if (updatedUser.password() != null && !updatedUser.password().isBlank()) {
            String encryptedPassword = passwordEncoder.encode(updatedUser.password());
            existingUser.setPassword(encryptedPassword);
        }

        User userAttach = userRepository.save(existingUser);

        UserResponseDTO userResponseDTO = new UserResponseDTO(userAttach.getId(),userAttach.getName(), userAttach.getEmail());

        return userResponseDTO;
    }
}
