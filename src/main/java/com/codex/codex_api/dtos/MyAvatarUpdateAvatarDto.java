package com.codex.codex_api.dtos;

import com.codex.codex_api.models.Avatar;
import jakarta.validation.constraints.NotNull;

public record MyAvatarUpdateAvatarDto(@NotNull Avatar avatarBase) {
}
