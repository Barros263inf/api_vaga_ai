package com.vaga.ai.gs.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
        @NotBlank(message = "{user.name.required}")
        @Size(min = 3, max = 50, message = "{user.name.short}")
        String name,

        @NotBlank(message = "{user.email.required}")
        @Email(message = "{user.email.invalid}")
        String email,

        @NotBlank(message = "{user.pass.required}")
        @Size(min = 8, max = 255, message = "{user.pass.short}")
        String password,

        @Pattern(regexp = "\\d{10,14}", message = "{user.phone.invalid}")
        String phone
) {
}
