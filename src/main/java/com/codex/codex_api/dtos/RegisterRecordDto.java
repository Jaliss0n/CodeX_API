package com.codex.codex_api.dtos;

import com.codex.codex_api.models.enums.UserRole;

public record RegisterRecordDto(String login, String password, UserRole role) {
}
