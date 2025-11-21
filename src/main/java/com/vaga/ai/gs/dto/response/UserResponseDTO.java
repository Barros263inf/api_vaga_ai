package com.vaga.ai.gs.dto.response;

import com.vaga.ai.gs.model.User;
import com.vaga.ai.gs.model.enums.Role;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String phone,
        Role role
) {
    // Construtor estático para facilitar a conversão Entity -> DTO
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }
}
