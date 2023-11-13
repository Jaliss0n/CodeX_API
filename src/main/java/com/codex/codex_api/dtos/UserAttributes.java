package com.codex.codex_api.dtos;

import com.codex.codex_api.models.enums.UserRole;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserAttributes {
    private UUID idAccess;
    private int idMoodle;
    private UserRole role;
    private String name;
    private List items;

    public UserAttributes(UUID idAccess, int idMoodle, UserRole role, String name, List items) {
        this.idAccess = idAccess;
        this.idMoodle = idMoodle;
        this.role = role;
        this.name = name;
        this.items = items;
    }

}
