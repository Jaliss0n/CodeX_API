package com.codex.codex_api.dtos;

import com.codex.codex_api.models.Item;
import com.codex.codex_api.models.Users;
import com.codex.codex_api.models.Avatar;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record MyAvatarDto (@NotNull Users user,@NotNull Avatar avatarBase){
}

