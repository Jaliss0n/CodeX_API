package com.codex.codex_api.dtos;

import com.codex.codex_api.models.Users;
import com.codex.codex_api.models.Avatar;

public record MyAvatarDto (Users user, Avatar avatarBase, String nameAvatar){
}
