package com.codex.codex_api.dtos;

import com.codex.codex_api.models.enums.UserRole;
import jakarta.validation.constraints.NotBlank;

public record RegisterUnityDTO(@NotBlank String login, @NotBlank String password, UserRole role, @NotBlank String name, @NotBlank String zipCode, @NotBlank String address) {
}
