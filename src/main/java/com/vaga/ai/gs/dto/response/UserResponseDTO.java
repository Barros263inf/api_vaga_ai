package com.vaga.ai.gs.dto.response;

import com.vaga.ai.gs.model.User;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String phone
) {
    // Construtor estático para facilitar a conversão Entity -> DTO
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }
}
