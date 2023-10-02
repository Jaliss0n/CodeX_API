package com.codex.codex_api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDto(@NotBlank String nameItem, @NotNull Integer type, @NotBlank String description, @NotNull Integer value,@NotNull Integer resaleValue, @NotBlank String uriImgItem) {
}
