package com.codex.codex_api.dtos;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordStudent(@NotBlank String login, @NotBlank String password) {

}
