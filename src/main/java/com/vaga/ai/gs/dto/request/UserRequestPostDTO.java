package com.vaga.ai.gs.dto.request;

import com.vaga.ai.gs.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestPostDTO(
        @NotBlank(message = "{user.name.required}")
        @Size(min = 3, max = 50, message = "{user.name.short}")
        String name,

        @NotBlank(message = "{user.email.required}")
        @Email(message = "{user.email.invalid}")
        String email,

        @NotBlank(message = "{user.pass.required}")
        @Size(min = 8, max = 12, message = "{user.pass.short}")
        String password,

        @Pattern(regexp = "\\d{10,14}", message = "{user.phone.invalid}")
        String phone,

        @Enumerated(EnumType.STRING)
        Role role

) {
}
