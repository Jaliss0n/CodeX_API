package com.codex.codex_api.dtos;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MyAvatarItemDTO(@NotNull UUID idItem) {
}
