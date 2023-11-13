package com.codex.codex_api.dtos;

import com.codex.codex_api.models.Users;
import com.codex.codex_api.models.enums.UserRole;

import java.util.UUID;

public record LoginResponseRecordDto(String token, UserAttributes userAttributes   ) {

}
