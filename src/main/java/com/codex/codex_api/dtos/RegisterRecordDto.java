package com.codex.codex_api.dtos;

import com.codex.codex_api.models.enums.UserRole;
import jakarta.validation.constraints.NotBlank;

public record RegisterRecordDto(@NotBlank String login, @NotBlank String password, UserRole role ) {
}
