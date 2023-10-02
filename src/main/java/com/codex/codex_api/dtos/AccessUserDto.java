package com.codex.codex_api.dtos;

import jakarta.validation.constraints.NotBlank;

public record AccessUserDto(@NotBlank String nameAdm, @NotBlank String login, @NotBlank String password, @NotBlank String typeAccess) {
}
